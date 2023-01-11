package lunatech.application.services.kafka

import akka.Done
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import lunatech.application.Server.executionContext
import lunatech.application.controllers.throwable.custom.KafkaProducerException
import org.apache.kafka.clients.producer.{ Callback, KafkaProducer, ProducerRecord, RecordMetadata }
import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ Future, Promise }
import scala.jdk.CollectionConverters._

object AppKafkaProducer {
  var counter: Int = 0
}

case class AppKafkaProducer[K: Encoder, V: Encoder](topic: String) {

  private val props: Map[String, Object] = Map(
    CLIENT_ID_CONFIG -> "producer_%d".format(AppKafkaConsumer.counter),
    BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
    KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
    VALUE_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
    ACKS_CONFIG -> "all"
  )

  private val producer: KafkaProducer[String, String] =
    new KafkaProducer[String, String](props.asJava)

  def produce(
      records: (K, V),
      callback: ((K, V)) => Unit
  ): Future[Done] = {

    val p: Promise[Done] = Promise()
    producer.send(
      new ProducerRecord[String, String](
        topic,
        records._1.asJson.spaces4SortKeys,
        records._2.asJson.spaces4SortKeys
      ),
      new Callback {
        override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit =
          if (exception != null) {
            exception.printStackTrace()
            p.failure(exception)
          } else {
            callback(records)
            p.success(Done)
          }
      }
    )
    p.future.recover(_ => throw new KafkaProducerException())
  }

  def free(): Unit =
    producer.close()
}
