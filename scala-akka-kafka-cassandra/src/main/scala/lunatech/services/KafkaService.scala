package lunatech.services

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import lunatech.Server.executionContext
import lunatech.domain.PersonEntity
import lunatech.services.kafka.AppKafkaProducer
import lunatech.services.kafka.AppKafkaConsumer

import scala.concurrent.Future

object KafkaService extends Service {

  private val personTopic: String                                 = "persons"
  private val personProducer: AppKafkaProducer[Int, PersonEntity] = AppKafkaProducer(personTopic)
  private val personConsumer: AppKafkaConsumer[Int, PersonEntity] = AppKafkaConsumer(personTopic)

  def alwaysProduce(): Future[Unit] =
    Future {
      try {
        var id: Int = 0;
        while (true) {

          val key   = id
          val value = PersonEntity(id, "name_%d".format(id))
          personProducer.produce(
            (key, value),
            res => println("Produced couple (%d, %s)".format(res._1, res._2))
          )
          Thread.sleep(2000)
          id += 1
        }
      } catch {
        case e: Exception =>
          recoverFunction(
            e,
            throw new IllegalStateException("KafkaService.alwaysProduce() failed!")
          )
      } finally {
        personProducer.free()
      }
    }

  def alwaysConsume(): Future[Unit] =
    Future {
      try {
        while (true) {
          personConsumer.consume(res => println("Consumed couple (%d, %s)".format(res._1, res._2)))
          Thread.sleep(2000)
        }
      } catch {
        case e: Exception =>
          recoverFunction(
            e,
            throw new IllegalStateException("KafkaService.alwaysConsume() failed!")
          )
      } finally {
        personConsumer.free()
      }
    }

  def run(): Unit = {
    alwaysProduce()
    alwaysConsume()
  }
}
