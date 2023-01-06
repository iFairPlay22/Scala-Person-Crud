package lunatech

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import lunatech.controllers.HttpController
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HttpControllerTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  val routes: Route = HttpController.routes

  "The service" should {

    "return a greeting for GET requests to the root path" in {
      // tests:
      Get() ~> routes ~> check {
        status.shouldEqual(StatusCodes.OK)
        responseAs[String].shouldEqual("Success".asJson.spaces4SortKeys)
      }
    }

  }
}
