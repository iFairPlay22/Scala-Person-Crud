package lunatech.application.repositories

import akka.{ Done, NotUsed }
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{
  CassandraSession,
  CassandraSessionRegistry,
  CassandraSource
}
import akka.stream.scaladsl.{ Sink, Source }
import com.datastax.oss.driver.api.core.cql.Row
import lunatech.application.Server.{ executionContext, system }

import scala.concurrent.Future

abstract class Repository {

  // keyspace universe
  val keyspace_universe_name = "universe"

  // table person
  val table_person_name            = "persons"
  val table_person_field_id_name   = "id"
  val table_person_field_name_name = "name"

  implicit val session: CassandraSession =
    CassandraSessionRegistry.get(system).sessionFor(CassandraSessionSettings())

  private def createSource(
      stmt: String,
      params: List[Any]
  ): Source[Row, NotUsed] =
    CassandraSource(
      stmt,
      params.map(e => e.asInstanceOf[AnyRef]): _*
    )

  def queryToEmptyResult(
      stmt: String,
      params: List[Any]
  ): Future[Done] =
    createSource(stmt, params)
      .runWith(Sink.ignore)

  def queryToSingleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Future[T] =
    createSource(stmt, params)
      .runWith(Sink.head)
      .map(castFunction)

  def queryToOptionalSingleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Future[Option[T]] =
    createSource(stmt, params)
      .runWith(Sink.headOption)
      .map { row =>
        if (row.nonEmpty) {
          Some(castFunction(row.get))
        } else {
          None
        }
      }

  def queryToMultipleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Source[T, NotUsed] =
    createSource(stmt, params)
      .map(castFunction)
}
