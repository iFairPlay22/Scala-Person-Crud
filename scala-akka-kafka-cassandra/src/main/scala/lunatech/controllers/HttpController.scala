package lunatech.controllers

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ Http, HttpExt }
import lunatech.Server.system
import lunatech.services.http._

object HttpController extends Controller {

  implicit val http: HttpExt = Http()

  override def routes: Route = path("http-request") {
    get(complete(toJson(HttpService.pingGoogle())))
  }
}
