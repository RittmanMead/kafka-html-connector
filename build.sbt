//import Dependencies.{Enumeratum, KafkaConnectApi, LogBack, Mockito, ScalaJ, ScalaLogging, ScalaTest, Scalatics}
//import sbt._
//import sbt.Keys.{libraryDependencies, _}

name := "kafka-http-connector"
version := "0.0.1"
organization := "com.rittmanmead"
scalaVersion := "2.12.8"

libraryDependencies += "javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts( Artifact("javax.ws.rs-api", "jar", "jar"))

libraryDependencies += "ch.qos.logback"              % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"   % "3.5.0"
libraryDependencies += "org.apache.kafka"            % "connect-api"     % "2.1.0"
libraryDependencies += "com.beachape"               %% "enumeratum"      % "1.5.12"
libraryDependencies += "org.scalactic"              %% "scalactic"       % "3.0.1"  % "test"
libraryDependencies += "org.scalatest"              %% "scalatest"       % "3.0.1"  % "test"
libraryDependencies += "org.mockito"                 % "mockito-core"    % "2.10.0" % "test"
libraryDependencies += "org.scalaj" %% "scalaj-http" %  "2.4.1"

//private val KafkaConnectApi     = "org.apache.kafka"            % "connect-api"     % "2.1.0" /* "0.9.0.0" */
//  private val ScalaJ              = "org.scalaj"                  % "scalaj-http_2.11" % "2.3.0"



/*

lazy val `kafka-http-connector` =
  (project in file("."))
    .settings(
      name := "kafka-http-connector",
      version := "0.0.1",
      organization := "com.rittmanmead",
      scalaVersion := "2.12.8",
      //crossScalaVersions := Seq("2.11.7", "2.12.2"),
      crossScalaVersions := Seq("2.12.8"),


      //libraryDependencies ++= Dependencies.Compile.kafkaHttpConnector ++ Dependencies.Test.kafkaHttpConnector,

      fork in Test := true
    )
    //.enablePlugins(BuildInfoPlugin)
    .settings(
      buildInfoKeys := Seq[BuildInfoKey](version),
      buildInfoPackage := organization.value
    )
    .settings(
      test in assembly := {},
      assemblyJarName in assembly := s"kafka-http-connector-${version.value}.jar"
    )
    .settings(
      useGpg := true,
      pgpPublicRing := file("~/.sbt/gpg/pubring.asc"),
      pgpSecretRing := file("~/.sbt/gpg/secring.asc"),
      publishTo := Some(
        if (isSnapshot.value) Opts.resolver.sonatypeSnapshots
        else Opts.resolver.sonatypeStaging
      ),
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishMavenStyle := true,
      licenses := Seq("Apache 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
      homepage := Some(url("https://github.com/agoda-com/kafka-jdbc-connector"))//,
      //scmInfo := Some(
      //  ScmInfo(
      //    url("https://github.com/agoda-com/kafka-jdbc-connector"),
      //    "scm:git@github.com:agoda-com/kafka-jdbc-connector.git"
      //  )
      //)//,
      //developers := List(
        //Developer(
        //  id="arpanchaudhury",
        //  name="Arpan Chaudhury",
        //  email="arpan.chaudhury@agoda.com",
        //  url=url("https://github.com/arpanchaudhury")
        //)
      //)
    )
    .settings(
      coverageExcludedPackages := Seq(
        "com.rittmanmead.BuildInfo",
        "com.rittmanmead.kafka.connector.http.HttpSourceConnector",
        "com.rittmanmead.kafka.connector.http.HttpSourceTask",
        "com.rittmanmead.kafka.connector.http.utils.Version"
      ).mkString(";")
    )
*/