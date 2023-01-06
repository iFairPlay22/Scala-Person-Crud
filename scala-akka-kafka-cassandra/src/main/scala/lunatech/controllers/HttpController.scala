package lunatech.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ Http, HttpExt }
import lunatech.Server.system
import lunatech.services.HttpService

object HttpController extends Controller {

  implicit val http: HttpExt = Http()

  override def routes: Route = path("http-request") {
    get(complete(toJson(HttpService.pingGoogle())))
  }
}
