package lunatech.domain

sealed trait Entity
case class PersonEntity(id: Int, name: String) extends Entity {
  require(0 <= id, "person id must be positive or null")
  require(name.nonEmpty, "person name must not be empty")
  require(3 <= name.length && name.length <= 25, "person name length must be between 0 and 25")
}
