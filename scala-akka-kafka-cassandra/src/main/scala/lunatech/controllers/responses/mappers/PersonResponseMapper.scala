package lunatech.controllers.responses.mappers

import lunatech.controllers.responses.data.PersonResponse
import lunatech.repositories.domain.PersonEntity

object PersonResponseMapper {

  def entityToResponse(person: PersonEntity): PersonResponse =
    PersonResponse(
      person.id,
      person.name
    )

}
