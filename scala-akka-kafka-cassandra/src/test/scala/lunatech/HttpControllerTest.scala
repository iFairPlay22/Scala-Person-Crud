package lunatech

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import lunatech.controllers.HttpController
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HttpControllerTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  val routes: Route = HttpController.routes

  "The service" should {
    "return a 'Success' for GET requests to the 'http-request' path" in {
      // tests:
      Get("http-request") ~> routes ~> check {
        status.shouldEqual(StatusCodes.OK)
        responseAs[String].shouldEqual("Success")
      }
    }
  }
}
