//import Dependencies.{Enumeratum, KafkaConnectApi, LogBack, Mockito, ScalaJ, ScalaLogging, ScalaTest, Scalatics}
//import sbt._
//import sbt.Keys.{libraryDependencies, _}

name := "kafka-http-connector"
organization := "com.rittmanmead"

version := "0.0.1"
scalaVersion := "2.12.8"

val kafkaVersion = "2.1.0"

libraryDependencies += "javax.ws.rs"                    %   "javax.ws.rs-api"   % "2.1.1" artifacts( Artifact("javax.ws.rs-api", "jar", "jar"))
libraryDependencies += "ch.qos.logback"                 %   "logback-classic"   % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging"     %%  "scala-logging"     % "3.5.0"
libraryDependencies += "org.apache.kafka"               %   "connect-api"       % kafkaVersion
libraryDependencies += "com.beachape"                   %%  "enumeratum"        % "1.5.12"
libraryDependencies += "org.scalactic"                  %%  "scalactic"         % "3.0.1"       % "test"
libraryDependencies += "org.scalatest"                  %%  "scalatest"         % "3.0.1"       % "test"
libraryDependencies += "org.mockito"                    %   "mockito-core"      % "2.10.0"      % "test"
libraryDependencies += "org.scalaj"                     %%  "scalaj-http"       %  "2.4.1"

