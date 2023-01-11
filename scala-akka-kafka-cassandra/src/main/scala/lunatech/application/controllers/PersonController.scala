package lunatech.application.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.Route
import lunatech.application.controllers.requests._
import lunatech.application.services.person._

object PersonController extends Controller {

  override def routes: Route =
    pathPrefix("person") {
      pathEnd {
        get {
          // TODO: Remove toJson()
          complete(toJson(PersonService.getPersonsResponse(GetPersonsRequest())))
        } ~
        post {
          parameters("name".as[String]) { name =>
            complete(toJson(PersonService.createPersonResponse(CreatePersonRequest(name))))
          }
        }
      } ~
      path(IntNumber) { id =>
        get {
          complete(toJson(PersonService.getPersonResponse(GetPersonRequest(id))))
        } ~
        delete {
          complete(toJson(PersonService.deletePersonResponse(DeletePersonRequest(id))))
        } ~
        put {
          parameters("name".as[String]) { name =>
            complete(toJson(PersonService.editPersonResponse(EditPersonRequest(id, name))))
          }
        }
      } ~
      path("from-external-api") {
        get {
          complete(
            toJson(
              PersonService.getPersonsFromExternalApiResponse(GetPersonsFromExternalApiRequest())
            )
          )
        }
      }
    }

}
