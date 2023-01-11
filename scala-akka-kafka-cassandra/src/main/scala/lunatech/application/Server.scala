package lunatech.application

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.{ Directives, Route }
import akka.http.scaladsl.{ Http, HttpExt }

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

import lunatech.application.controllers._
import lunatech.application.services.kafka._

object Server extends Directives {

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-actor-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val http: HttpExt                              = Http()

  def main(args: Array[String]): Unit = {

    // Launch server
    val httpServer = http
      .newServerAt("localhost", 8080)
      .bind(Route.seal(PersonController.routes))

    KafkaService.run()

    // Stop server
    println(s"Server now online.\nPress RETURN to stop...")
    StdIn.readLine()
    httpServer
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
