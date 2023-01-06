package lunatech.repositories

import com.datastax.oss.driver.api.core.cql.Row
import akka.{ Done, NotUsed }
import akka.stream.scaladsl.Source
import lunatech.domain.PersonEntity

import scala.concurrent.Future

object TablePersonRepository extends Repository {

  def count(): Future[Int] =
    queryToSingleResult(
      "SELECT CAST(COUNT(id) + 1 AS INT) AS persons_sum FROM %s.%s"
        .format(keyspace_universe_name, table_person_name),
      List(),
      row => row.getInt("persons_sum")
    )

  def insert(person: PersonEntity): Future[Done] =
    queryToEmptyResult(
      "INSERT INTO %s.%s(id, name) VALUES (?, ?)"
        .format(keyspace_universe_name, table_person_name),
      List(
        person.id,
        person.name
      )
    )

  def edit(person: PersonEntity): Future[Done] =
    queryToEmptyResult(
      "UPDATE %s.%s SET name = ? WHERE id = ?"
        .format(keyspace_universe_name, table_person_name),
      List(
        person.name,
        person.id
      )
    )

  def delete(id: Int): Future[Done] =
    queryToEmptyResult(
      "DELETE FROM %s.%s WHERE id = ?"
        .format(keyspace_universe_name, table_person_name),
      List(id)
    )

  def selectAll(): Source[PersonEntity, NotUsed] =
    queryToMultipleResult(
      "SELECT * FROM %s.%s".format(keyspace_universe_name, table_person_name),
      List(),
      mapRowToPersonEntity
    )

  def selectOne(id: Int): Future[Option[PersonEntity]] =
    queryToOptionalSingleResult(
      "SELECT * FROM %s.%s WHERE id = ?"
        .format(keyspace_universe_name, table_person_name),
      List(id),
      mapRowToPersonEntity
    )

  // Transform a com.datastax.oss.driver.api.core.cql.Row to a PersonEntity
  private def mapRowToPersonEntity(row: Row): PersonEntity =
    PersonEntity(
      row.getInt(table_person_field_id_name),
      row.getString(table_person_field_name_name)
    )

}
