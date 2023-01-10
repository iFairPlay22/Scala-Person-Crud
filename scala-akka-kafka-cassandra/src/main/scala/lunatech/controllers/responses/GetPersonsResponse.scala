package lunatech.controllers.responses

import lunatech.controllers.responses.data._

// TODO: Replace Seq[PersonEntity] by Source[PersonResponse, NotUsed]
case class GetPersonsResponse(users: Seq[PersonResponse]) extends Response
