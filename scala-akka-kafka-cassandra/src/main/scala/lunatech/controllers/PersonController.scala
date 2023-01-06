package lunatech.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.Route
import lunatech.controllers.requests._
import lunatech.services.PersonService

object PersonController extends Controller {

  override def routes: Route =
    pathPrefix("person") {
      pathEnd {
        get {
          // TODO: Remove toJson()
          complete(toJson(PersonService.getPersons(GetPersonsRequest())))
        } ~
        post {
          parameters("name".as[String]) { name =>
            complete(toJson(PersonService.createPerson(CreatePersonRequest(name))))
          }
        }
      } ~
      path(IntNumber) { id =>
        get {
          complete(toJson(PersonService.getPerson(GetPersonRequest(id))))
        } ~
        delete {
          complete(toJson(PersonService.deletePerson(DeletePersonRequest(id))))
        } ~
        put {
          parameters("name".as[String]) { name =>
            complete(toJson(PersonService.editPerson(EditPersonRequest(id, name))))
          }
        }
      }
    }

}
