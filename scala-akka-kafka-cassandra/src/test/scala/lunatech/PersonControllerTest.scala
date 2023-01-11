package lunatech

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import lunatech.application.controllers.PersonController
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PersonControllerTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  val routes: Route = PersonController.routes

  "The service" should {
    "return a 'Success' for GET requests to the 'http-request' path" in {
      // tests:
      Get("person") ~> routes ~> check {
        status.shouldEqual(StatusCodes.OK)
      }
    }
  }
}
