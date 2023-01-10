package lunatech.services.http

import akka.http.scaladsl.model.HttpRequest
import lunatech.Server._
import lunatech.services.Service

import scala.concurrent.Future

object HttpService extends Service {

  def pingGoogle(): Future[String] =
    http
      .singleRequest(HttpRequest(uri = "https://www.google.fr/"))
      .map { res =>
        if (res.status.isSuccess())
          "Success"
        else
          throw new IllegalStateException("Ping google failed!")
      }

}
