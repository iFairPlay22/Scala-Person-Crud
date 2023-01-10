package lunatech.services.person

import akka.stream.scaladsl.Sink
import lunatech.controllers.requests._
import lunatech.controllers.responses._
import lunatech.controllers.responses.mappers._
import lunatech.repositories._
import lunatech.repositories.domain._
import lunatech.Server._
import lunatech.services.Service

import scala.concurrent.Future

object PersonService extends Service {

  def createPerson(name: String): Future[PersonEntity] =
    for {
      id <- TablePersonRepository
        .count()
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't create person: count() failure")
          )
        }
      person <- Future(PersonEntity(id, name))
      _ <- TablePersonRepository
        .insert(person)
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't create person: unexpected error")
          )
        }
    } yield person

  def createPersonResponse(req: CreatePersonRequest): Future[CreatePersonResponse] =
    for {
      person <- createPerson(req.name)
    } yield CreatePersonResponse {
      PersonResponseMapper.entityToResponse(person)
    }

  def editPerson(id: Int, name: String): Future[PersonEntity] = {

    val person = PersonEntity(id, name)
    for {
      maybePerson <- TablePersonRepository
        .selectOne(id)
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't edit person: unexpected error when get person")
          )
        }
      person <- Future {
        if (maybePerson.isEmpty)
          throw new NoSuchElementException("Can't edit person: unassigned id")
        maybePerson.get
      }
      _ <- TablePersonRepository
        .edit(person)
        .recover(s_err =>
          recoverFunction(
            s_err,
            new NoSuchElementException("Can't edit person: id not exists")
          )
        )
    } yield person
  }

  def editPersonResponse(req: EditPersonRequest): Future[EditPersonResponse] =
    for {
      person <- editPerson(req.id, req.name)
    } yield EditPersonResponse {
      PersonResponseMapper.entityToResponse(person)
    }

  // TODO
  def getPersons(): Future[Seq[PersonEntity]] =
    for {
      persons <- TablePersonRepository
        .selectAll()
        // TODO: Remove .runWith(Sink.seq)
        .runWith(Sink.seq)
        .recover(s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't get person: unexpected error")
          )
        )
    } yield persons

  def getPersonsResponse(req: GetPersonsRequest): Future[GetPersonsResponse] =
    for {
      persons <- getPersons()
    } yield GetPersonsResponse {
      persons.map(person => PersonResponseMapper.entityToResponse(person))
    }

  def getPerson(id: Int): Future[PersonEntity] =
    for {
      maybePerson <- TablePersonRepository
        .selectOne(id)
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't get person: unexpected error")
          )
        }
      person <- Future {
        if (maybePerson.isEmpty)
          throw new NoSuchElementException("Can't get person: unassigned id")
        maybePerson.get
      }
    } yield person

  def getPersonResponse(req: GetPersonRequest): Future[GetPersonResponse] =
    for {
      person <- getPerson(req.id)
    } yield GetPersonResponse {
      PersonResponseMapper.entityToResponse(person)
    }

  def deletePerson(id: Int): Future[Int] =
    for {
      _ <- TablePersonRepository
        .delete(id)
        .recover(s_err =>
          recoverFunction(
            s_err,
            new NoSuchElementException("Can't delete person: unassigned id")
          )
        )
    } yield id

  def deletePersonResponse(req: DeletePersonRequest): Future[DeletePersonResponse] =
    for {
      id <- deletePerson(req.id)
    } yield DeletePersonResponse {
      id
    }

}
