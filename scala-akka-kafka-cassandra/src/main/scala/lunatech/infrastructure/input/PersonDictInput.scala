package lunatech.infrastructure.input

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

case class PersonDictInput(persons: List[PersonInput]) extends Input {}
