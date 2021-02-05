# Spring Kafka Introduction

[![Build Status](https://travis-ci.org/mguenther/spring-kafka-introduction.svg?branch=master)](https://travis-ci.org/mguenther/spring-kafka-introduction.svg)

This repository contains three Maven modules that showcase how to implement various Kafka producers and consumers using [Spring for Apache Kafka](https://spring.io/projects/spring-kafka) (2.6.5). The code presented in this repository is the joint work of [Boris Fresow](mailto://bfresow@gmail.com) and [Markus Günther](mailto://markus.guenther@gmail.com) as part of an article series on **Building Event-based applications with Spring Kafka** for the German [JavaMagazin](https://jaxenter.de/magazine/java-magazin).

## Modules

This repository is structured into several smaller Maven modules, each highlighting a different aspect.

| Module | Purpose |
| ------ | ------- |
| `springkafka-simple-producer-consumer` | Contains an implementation of a very simple producer submits `String`-based messages to a Kafka topic and an equally simple consumer that reads the messages from that same topic and stores them in-memory. Uses a Bean-based configuration. |
| `springkafka-result-aware-producer-consumer` | This example builds on the simple producer and consumer scenario and adds callbacks to the producer, so that the application can act upon the success or failure of writing messages to a Kafka topic. The consumer on the other hand uses explicit acknowledgements and thus manually commits consumer offsets back to the Kafka cluster. This enables the application to properly execute further actions on consumed messages until they are acknowledged. Uses a Property-based configuration. This module also showcases how to write integration tests using `spring-kafka-test`. |
| `springkafka-transactions` | This example shows how to implement a producer that is both idempotent and transactional. Uses Bean-based configuration. |

## Prerequisites

Running the showcase requires a working installation of Apache ZooKeeper and Apache Kafka. We provide `Dockerfile`s for both of them to get you started easily. Please make sure that [Docker](https://docs.docker.com/engine/installation/) as well as [Docker Compose](https://docs.docker.com/compose/install/) are installed on your system.

### Versions

| Application         | Version   | Docker Image            |
| ------------------- | --------- | ----------------------- |
| Apache Kafka        | 2.6.0  | wurstmeister/kafka:2.13-2.6.0     |
| Apache ZooKeeper    | 3.4.13   | wurstmeister/zookeeper |

### Building and Running the Containers

Start the resp. containers using the provided `docker-compose` script (cf. `docker/docker-compose.yml`). Simply issue

```bash
$ docker-compose up
```

for starting Apache Kafka and Apache Zookeeper. Stopping the containers is best done using a separate terminal and issueing the following commands.

```bash
$ docker-compose stop
$ docker-compose rm
```

The final ```rm``` operation deletes the containers and thus clears all state so you can start over with a clean installation.

For simplicity, we restrict the Kafka cluster to a single Kafka broker. However, scaling to more Kafka brokers is easily done via `docker-compose`. You will have to provide a sensible value for `KAFKA_ADVERTISED_HOST_NAME` (other than `localhost`) for this to work, though. 

```bash
$ docker-compose scale kafka=3   # scales up to 3 Kafka brokers
$ docker-compose scale kafka=1   # scales down to 1 Kafka broker after the previous upscale
```

After changing the number of Kafka brokers, give the cluster some time so that all brokers can finish their cluster-join procedure. This should complete in a couple of seconds and you can inspect the output of the resp. Docker containers just to be sure that everything is fine. Kafka Manager should also reflect the change in the number of Kafka brokers after they successfully joined the cluster.

## License

This work is released under the terms of the MIT license.