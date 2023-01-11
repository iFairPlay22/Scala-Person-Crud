package lunatech.application.services.kafka

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import lunatech.application.controllers.throwable.custom.KafkaConsumerException
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig._
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import scala.jdk.CollectionConverters._

object AppKafkaConsumer {
  var counter: Int = 0
}

case class AppKafkaConsumer[K: Decoder, V: Decoder](topic: String) {

  private val props: Map[String, Object] = Map(
    GROUP_ID_CONFIG -> "group_id",
    CLIENT_ID_CONFIG -> "consumer_%d".format(AppKafkaConsumer.counter),
    BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
    KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
  )

  private val consumer: KafkaConsumer[String, String] =
    new KafkaConsumer[String, String](props.asJava)

  consumer.subscribe(List(topic).asJava)
  AppKafkaConsumer.counter += 1

  def consume(
      callback: ((K, V)) => Unit
  ): Unit =
    for (record <- consumer.poll(Duration.ofSeconds(5)).asScala) {
      callback(
        (
          decode[K](record.key()).getOrElse(throw new KafkaConsumerException()),
          decode[V](record.value()).getOrElse(throw new KafkaConsumerException())
        )
      )
    }

  def free(): Unit =
    consumer.close()
}
