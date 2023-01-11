package lunatech.application.controllers.responses.mappers

import lunatech.application.controllers.responses.data.PersonResponse
import lunatech.domain.PersonEntity

object PersonResponseMapper {

  def entityToResponse(person: PersonEntity): PersonResponse =
    PersonResponse(
      person.id,
      person.name
    )

}
