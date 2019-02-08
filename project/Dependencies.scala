import sbt._

object Dependencies {
  private val ScalaTestV = "3.0.1"

  private val LogBack             = "ch.qos.logback"              % "logback-classic" % "1.2.3"
  private val ScalaLogging        = "com.typesafe.scala-logging" %% "scala-logging"   % "3.5.0"
  //private val KafkaConnectApi     = "org.apache.kafka"            % "connect-api"     % "2.1.0" /* "0.9.0.0" */
  private val KafkaConnectApi     = "org.apache.kafka"            % "connect-api"     % "2.1.0"
  private val Enumeratum          = "com.beachape"               %% "enumeratum"      % "1.5.12"
  private val Scalatics           = "org.scalactic"              %% "scalactic"       % ScalaTestV  % "test"
  private val ScalaTest           = "org.scalatest"              %% "scalatest"       % ScalaTestV  % "test"
  private val Mockito             = "org.mockito"                 % "mockito-core"    % "2.10.0"     % "test"

//  private val ScalaJ              = "org.scalaj"                  % "scalaj-http_2.11" % "2.3.0"

  private val ScalaJ              = "org.scalaj" %% "scalaj-http" % "2.4.1"


  object Compile {
    def kafkaHttpConnector  = Seq(LogBack, ScalaLogging, KafkaConnectApi, Enumeratum, ScalaJ)
  }

  object Test {
    def kafkaHttpConnector = Seq(LogBack, ScalaLogging, Scalatics, ScalaTest, Mockito)
  }
}