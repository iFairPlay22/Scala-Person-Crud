package lunatech.infrastructure.providers

import akka.http.scaladsl.common.{ EntityStreamingSupport, JsonEntityStreamingSupport }
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.circe.parser.decode
import io.circe.generic.auto._, io.circe.syntax._
import lunatech.domain._
import lunatech.application.Server._
import lunatech.application.controllers.throwable.custom.UnableToFetchPersonInInfraException
import lunatech.infrastructure.input.PersonDictInput
import lunatech.infrastructure.input.PersonInput
import lunatech.infrastructure.mappers.PersonInputMapper

import scala.concurrent.Future

object PersonProvider {
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()
  // URL used for demo... But it should obviously be an external URL...
  val persons_api_url: String = "http://localhost:8080/person"

  def getPersons(): Future[List[PersonEntity]] =
    for {
      req <- http.singleRequest(HttpRequest(uri = persons_api_url))
      json <- {
        if (req.status.isFailure())
          throw new IllegalStateException(
            "Can't get vehicles from %s: http request failed!".format(persons_api_url)
          )
        Unmarshal(req.entity).to[String]
      }
    } yield {
      val eitherErrorOrVehicles = decode[PersonDictInput](json)
      if (eitherErrorOrVehicles.isLeft) {
        eitherErrorOrVehicles.left.getOrElse(new Exception()).printStackTrace()
        throw new UnableToFetchPersonInInfraException()
      }
      eitherErrorOrVehicles
        .getOrElse(null)
        .persons
        .map(PersonInputMapper.inputToEntity)
    }
}
