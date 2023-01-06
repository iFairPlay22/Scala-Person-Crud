lazy val akkaHttpVersion = "10.4.0"
lazy val akkaVersion     = "2.7.0"
lazy val circeVersion    = "0.14.3"
lazy val kafkaVersion    = "3.3.1"
lazy val kafkaAvro       = "5.3.0"
lazy val avro4s          = "4.1.0"

fork := true

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "lunatech",
      scalaVersion := "2.13.4"
    )
  ),
  name := "scala-akka-kafka-cassandra",
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  libraryDependencies ++= Seq(
    // akka
    "com.typesafe.akka" %% "akka-http"        % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
    // akka + cassandra
    "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.1.0",
    // circe
    "io.circe" %% "circe-core"    % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser"  % circeVersion,
    // akka + circe
    "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",
    // kafka
    "org.apache.kafka"  % "kafka-clients"       % kafkaVersion,
    "org.apache.kafka"  % "kafka-streams"       % kafkaVersion,
    "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion,
    // avro
    "com.sksamuel.avro4s" % "avro4s-core_2.13"  % avro4s,
    "com.sksamuel.avro4s" % "avro4s-kafka_2.13" % avro4s,
    // kafka + avro
    "io.confluent" % "kafka-avro-serializer"    % kafkaAvro,
    "io.confluent" % "kafka-streams-avro-serde" % kafkaAvro,
    // util
    "com.softwaremill.common" %% "tagging"          % "2.3.4",
    "com.datastax.oss"         % "java-driver-core" % "4.15.0",
    "ch.qos.logback"           % "logback-classic"  % "1.4.5",
    // akka tests
    "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
    // scala tests
    "org.scalatest" %% "scalatest" % "3.2.14" % Test
  )
)
