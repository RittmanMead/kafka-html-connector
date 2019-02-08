package com.rittmanmead.kafka.connector.http


import com.rittmanmead.kafka.connector.http.HttpSourceConnectorConstants._
import com.rittmanmead.kafka.connector.http.models.Mode.{IncrementingMode, TimestampIncrementingMode, TimestampMode}
import scalaj.http.BuildInfo


/**
  * @constructor
  * @param connectorProperties is set of configurations required to create JdbcSourceConnectorConfig
  */
class HttpSourceConnectorConfig(val connectorProperties: Map[String, String]) {

  require(
    connectorProperties.contains(HTTP_URL_CONFIG) &&
        connectorProperties.contains(SERVICE_CONFIG) &&
        connectorProperties.contains(TOPIC_CONFIG) &&
        connectorProperties.contains(TASKS_MAX_CONFIG) &&
        connectorProperties.contains(CONNECTOR_CLASS) &&
        connectorProperties.contains(POLL_INTERVAL_MS_CONFIG),

    s"""Missing properties for Http Source Connector. Required:
       | $HTTP_URL_CONFIG
       | $SERVICE_CONFIG
       | $TOPIC_CONFIG
       | $TASKS_MAX_CONFIG
       | $CONNECTOR_CLASS
       | $POLL_INTERVAL_MS_CONFIG""".stripMargin
  )

  /**
  * @return database connecti
    url
  */
  def getHttpUrl: String = connectorProperties(HTTP_URL_CONFIG)

  /**
    * @return service name
    */
  def getService: String = connectorProperties(SERVICE_CONFIG)

  /**
    * @return kafka topic name
    */
  def getTopic: String = connectorProperties(TOPIC_CONFIG)

  /**
    * @return database poll interval
    */
  def getPollInterval: Long = connectorProperties.getOrElse(POLL_INTERVAL_MS_CONFIG, POLL_INTERVAL_MS_DEFAULT).toLong
}