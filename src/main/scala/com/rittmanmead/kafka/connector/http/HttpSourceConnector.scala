package com.rittmanmead.kafka.connector.http

import java.util

import com.rittmanmead.kafka.connector.http.utils.Version
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.source.{SourceConnector, SourceTask}
import org.slf4j.LoggerFactory
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.common.config.ConfigDef.Importance
import org.apache.kafka.common.config.ConfigDef.Type
import org.apache.kafka.common.config.ConfigDef.Width

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class HttpSourceConnector extends SourceConnector {
  private val logger = LoggerFactory.getLogger(classOf[HttpSourceConnector])

  private var myConfig: HttpSourceConnectorConfig = _

  private val configDef: ConfigDef = (new ConfigDef()
      .define(HttpSourceConnectorConstants.TOPIC_CONFIG, Type.STRING, Importance.HIGH, "Kafka Topic name")
      )

  /*
  val STORED_PROCEDURE_NAME_KEY           = "stored-procedure.name"

  val CONNECTION_URL_CONFIG               = "connection.url"

  val MODE_CONFIG                         = "mode"
  val TIMESTAMP_VARIABLE_NAME_CONFIG      = "timestamp.variable.name"
  val TIMESTAMP_FIELD_NAME_CONFIG         = "timestamp.field.name"
  val INCREMENTING_VARIABLE_NAME_CONFIG   = "incrementing.variable.name"
  val INCREMENTING_FIELD_NAME_CONFIG      = "incrementing.field.name"

  val STORED_PROCEDURE_NAME_CONFIG        = "stored-procedure.name"

  val TOPIC_CONFIG                        = "topic"

  val POLL_INTERVAL_MS_CONFIG             = "poll.interval.ms"
  val POLL_INTERVAL_MS_DEFAULT            = "5000"

  val BATCH_MAX_ROWS_VARIABLE_NAME_CONFIG = "batch.max.rows.variable.name"
  val BATCH_MAX_ROWS_CONFIG               = "batch.max.records"
  val BATCH_MAX_ROWS_DEFAULT              = "100"

  val TIMESTAMP_OFFSET_CONFIG             = "timestamp.offset"
  val TIMESTAMP_OFFSET_DEFAULT            = new Timestamp(0L)
  val INCREMENTING_OFFSET_CONFIG          = "incrementing.offset"
  val INCREMENTING_OFFSET_DEFAULT         = "0"

  val KEY_FIELD_NAME_CONFIG               = "key.field.name"

  val TASKS_MAX_CONFIG                    = "tasks.max"
  val CONNECTOR_CLASS                     = "connector.class"

   */



  override def config: ConfigDef = configDef



  /**
  * @return version of this connector
  */
  override def version: String = Version.getVersion

/**
  * invoked by kafka-connect runtime to start this connector
  *
  * @param connectorProperties properties required to start this connector
  */
  override def start(connectorProperties: util.Map[String, String]): Unit = {
    Try (new HttpSourceConnectorConfig(connectorProperties.asScala.toMap)) match {
      case Success(conf) => myConfig = conf
      case Failure(e) => logger.error(s"Couldn't start ${this.getClass.getName} due to configuration error", new ConnectException(e))
    }
  }

/**
  * invoked by kafka-connect runtime to stop this connector
  */
  override def stop(): Unit = {
    logger.debug(s"Stopping kafka source connector ${this.getClass.getName}")
  }

/**
  * invoked by kafka-connect runtime to instantiate SourceTask which polls data from external data store and saves into kafka
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
  override def taskConfigs(maxTasks: Int): util.List[util.Map[String, String]] = List(myConfig.connectorProperties.asJava).asJava


}