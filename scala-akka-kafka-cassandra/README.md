# Project

Simple project with Scala, Akka, Cassandra, Kafka and Circe.

## Setup the environment 

Launch the services:

    docker-compose up

Start the project:

    sbt run

## Interacting with the sample

Create the database:

    curl --location --request POST 'http://127.0.0.1:8080/init-db'

Insert a person:

    curl --location --request POST 'http://127.0.0.1:8080/person?name={PersonName}'

Edit a person:

    curl --location --request PUT 'http://127.0.0.1:8080/person/{PersonId}?name={PersonName}'

Delete a person:

    curl --location --request DELETE 'http://127.0.0.1:8080/person/{PersonId}'

Get a user:

    curl --location --request GET 'http://127.0.0.1:8080/person/{PersonId}'

Get all the users:

    curl --location --request GET 'http://127.0.0.1:8080/person'

Ping google:

    curl --location --request GET 'http://127.0.0.1:8080/http-request'
