package lunatech.application.controllers.responses

import lunatech.application.controllers.responses.data._

// TODO: Replace Seq[PersonEntity] by Source[PersonResponse, NotUsed]
case class GetPersonsResponse(persons: Seq[PersonResponse]) extends Response
