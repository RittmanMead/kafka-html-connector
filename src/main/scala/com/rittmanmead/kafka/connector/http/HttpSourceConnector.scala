package com.rittmanmead.kafka.connector.http

import java.util.{List => JavaList, Map => JavaMap}

import com.rittmanmead.kafka.connector.http.HttpSourceConnectorConstants.{API_KEY_CONFIG, API_PARAMS_CONFIG}
import org.slf4j.LoggerFactory
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.source.{SourceConnector, SourceTask}
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.common.config.ConfigDef.Importance
import org.apache.kafka.common.config.ConfigDef.Type

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}


class HttpSourceConnector extends SourceConnector {
  private val connectorLogger = LoggerFactory.getLogger(classOf[HttpSourceConnector])
  private var connectorConfig: HttpSourceConnectorConfig = _

  private val configDef: ConfigDef =
      new ConfigDef()
          .define(HttpSourceConnectorConstants.HTTP_URL_CONFIG, Type.STRING, Importance.HIGH, "Web API Access URL")
          .define(HttpSourceConnectorConstants.API_KEY_CONFIG, Type.STRING, Importance.HIGH, "Web API Access Key")
          .define(HttpSourceConnectorConstants.API_PARAMS_CONFIG, Type.STRING, Importance.HIGH, "Web API additional config parameters")
          .define(HttpSourceConnectorConstants.SERVICE_CONFIG, Type.STRING, Importance.HIGH, "Kafka Service name")
          .define(HttpSourceConnectorConstants.TOPIC_CONFIG, Type.STRING, Importance.HIGH, "Kafka Topic name")
          .define(HttpSourceConnectorConstants.POLL_INTERVAL_MS_CONFIG, Type.STRING, Importance.HIGH, "Polling interval in milliseconds")
          .define(HttpSourceConnectorConstants.TASKS_MAX_CONFIG, Type.INT, Importance.HIGH, "Kafka Connector Max Tasks")
          .define(HttpSourceConnectorConstants.CONNECTOR_CLASS, Type.STRING, Importance.HIGH, "Kafka Connector Class Name (full class path)")

  override def config: ConfigDef = configDef

/**
  * @return the version of this connector
  */
  override def version: String = HttpSourceVersion.getVersion

/**
  * invoked by Kafka connect runtime to start this connector
  *
  * @param connectorProperties properties required to start this connector
  */
  override def start(connectorProperties: JavaMap[String, String]): Unit = {
    Try (new HttpSourceConnectorConfig(connectorProperties.asScala.toMap)) match {
      case Success(cfg) => connectorConfig = cfg
      case Failure(err) => connectorLogger.error(s"Could not start Kafka Source Connector ${this.getClass.getName} due to error in configuration.", new ConnectException(err))
    }
  }

/**
  * invoked by Kafka connect runtime to stop this connector
  */
  override def stop(): Unit = {
    connectorLogger.info(s"Stopping Kafka Source Connector ${this.getClass.getName}.")
  }

/**
  * invoked by Kafka connect runtime to instantiate SourceTask which polls data from external data store and saves into Kafka
  *
  * @return class of source task to be created
  */
  override def taskClass(): Class[_ <: SourceTask] = classOf[HttpSourceTask]

/**
  * returns a set of configurations for tasks based on the current configuration
  *
  * @param maxTasks maximum number of configurations to generate
  * @return configurations for tasks
  */
  override def taskConfigs(maxTasks: Int): JavaList[JavaMap[String, String]] = List(connectorConfig.connectorProperties.asJava).asJava // Only returns one element in list. This assumes maxTasks = 1.

}