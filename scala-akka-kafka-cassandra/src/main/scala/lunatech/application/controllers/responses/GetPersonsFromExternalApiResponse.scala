package lunatech.application.controllers.responses

import lunatech.application.controllers.responses.data._

case class GetPersonsFromExternalApiResponse(persons: Seq[PersonResponse]) extends Response
