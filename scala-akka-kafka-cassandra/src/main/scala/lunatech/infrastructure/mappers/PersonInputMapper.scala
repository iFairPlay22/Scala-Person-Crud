package lunatech.infrastructure.mappers

import lunatech.domain.PersonEntity
import lunatech.infrastructure.input.PersonInput

object PersonInputMapper {

  def inputToEntity(person: PersonInput): PersonEntity =
    PersonEntity(
      person.id,
      person.name
    )
}
