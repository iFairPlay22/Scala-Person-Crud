package lunatech.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.{ Directives, Route }
import io.circe.Encoder
import lunatech.Server.executionContext

import scala.concurrent.Future

trait Controller extends Directives {
  def routes: Route

  def toJson[K: Encoder](k: Future[K]): Future[String] =
    k.map(e => e.asJson.spaces4SortKeys)
}
