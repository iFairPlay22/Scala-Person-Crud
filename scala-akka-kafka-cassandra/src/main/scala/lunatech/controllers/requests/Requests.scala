package lunatech.controllers.requests

// the requests the client can ask to the server
sealed trait Request
case class CreatePersonRequest(name: String) extends Request
case class EditPersonRequest(id: Int, name: String) extends Request
case class DeletePersonRequest(id: Int) extends Request
case class GetPersonRequest(id: Int) extends Request
case class GetPersonsRequest() extends Request
