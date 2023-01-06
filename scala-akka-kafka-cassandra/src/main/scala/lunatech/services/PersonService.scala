package lunatech.services

import akka.stream.scaladsl.Sink
import lunatech.Server.executionContext
import lunatech.Server.http.system
import lunatech.controllers.requests._
import lunatech.controllers.responses._
import lunatech.domain.PersonEntity
import lunatech.repositories.TablePersonRepository

import scala.concurrent.Future

object PersonService extends Service {

  def createPerson(req: CreatePersonRequest): Future[CreatePersonResponse] =
    for {
      id <- TablePersonRepository
        .count()
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't create person: count() failure")
          )
        }
      person <- Future(PersonEntity(id, req.name))
      _ <- TablePersonRepository
        .insert(person)
        .recover { s_err =>
          recoverFunction(
            s_err,
            new IllegalStateException("Can't create person: unexpected error")
          )
        }
    } yield CreatePersonResponse(person)

  def editPerson(req: EditPersonRequest): Future[EditPersonResponse] = {

    val person = PersonEntity(req.id, req.name)
    for {
      maybePerson <- TablePersonRepository
        .selectOne(req.id)
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
    } yield EditPersonResponse(person)
  }

  // TODO
  def getPersons(req: GetPersonsRequest): Future[GetPersonsResponse] =
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
    } yield GetPersonsResponse(persons)

  def getPerson(req: GetPersonRequest): Future[GetPersonResponse] =
    for {
      maybePerson <- TablePersonRepository
        .selectOne(req.id)
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
    } yield GetPersonResponse(person)

  def deletePerson(req: DeletePersonRequest): Future[DeletePersonResponse] =
    for {
      _ <- TablePersonRepository
        .delete(req.id)
        .recover(s_err =>
          recoverFunction(
            s_err,
            new NoSuchElementException("Can't delete person: unassigned id")
          )
        )
    } yield DeletePersonResponse(req.id)
}
