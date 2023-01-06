package lunatech.controllers.responses

import lunatech.domain.PersonEntity

// the responses it may send to the server
sealed trait Response
case class CreatePersonResponse(user: PersonEntity) extends Response
case class EditPersonResponse(user: PersonEntity) extends Response
case class GetPersonResponse(user: PersonEntity) extends Response
// TODO: Replace Seq[PersonEntity] by Source[PersonEntity, NotUsed]
case class GetPersonsResponse(users: Seq[PersonEntity]) extends Response
case class DeletePersonResponse(id: Int) extends Response
