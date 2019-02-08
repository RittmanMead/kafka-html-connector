package com.rittmanmead.kafka.connector.http

import java.sql.Timestamp
import java.util.TimeZone

import com.rittmanmead.kafka.connector.http.HttpSourceConnectorConstants._
import com.rittmanmead.kafka.connector.http.models.Mode.{IncrementingMode, TimestampIncrementingMode, TimestampMode}
import com.rittmanmead.kafka.connector.http.models.Mode

/**
  * @constructor
  * @param connectorProperties is set of configurations required to create JdbcSourceConnectorConfig
  */
class HttpSourceConnectorConfig(val connectorProperties: Map[String, String]) {

  require(
    connectorProperties.contains(HTTP_URL_CONFIG) &&
    //connectorProperties.contains(MODE_CONFIG) &&
    //connectorProperties.contains(STORED_PROCEDURE_NAME_CONFIG) &&
        connectorProperties.contains(TOPIC_CONFIG) &&
        connectorProperties.contains(SERVICE_CONFIG), //&&



    //connectorProperties.contains(BATCH_MAX_ROWS_VARIABLE_NAME_CONFIG),

    /*
    s"""Required connector properties:
       |  $HTTP_URL_CONFIG
       |  $MODE_CONFIG
       |  $STORED_PROCEDURE_NAME_CONFIG
       |  $TOPIC_CONFIG
       |  $BATCH_MAX_ROWS_VARIABLE_NAME_CONFIG""".stripMargin
       */

  s"""Required connector properties:
     |  $HTTP_URL_CONFIG
     |  $TOPIC_CONFIG
     |  $SERVICE_CONFIG""".stripMargin

  )

  /*
  require(
    Mode.withName(connectorProperties(MODE_CONFIG)) match {
      case TimestampMode              => connectorProperties.contains(TIMESTAMP_VARIABLE_NAME_CONFIG) &&
                                         connectorProperties.contains(TIMESTAMP_FIELD_NAME_CONFIG)
      case IncrementingMode           => connectorProperties.contains(INCREMENTING_VARIABLE_NAME_CONFIG) &&
                                         connectorProperties.contains(INCREMENTING_FIELD_NAME_CONFIG)
      case TimestampIncrementingMode  => connectorProperties.contains(TIMESTAMP_VARIABLE_NAME_CONFIG) &&
                                         connectorProperties.contains(TIMESTAMP_FIELD_NAME_CONFIG) &&
                                         connectorProperties.contains(INCREMENTING_VARIABLE_NAME_CONFIG) &&
                                         connectorProperties.contains(INCREMENTING_FIELD_NAME_CONFIG)
    },
    Mode.withName(connectorProperties(MODE_CONFIG)) match {
      case TimestampMode              => s"""Required connector properties:
                                             |  $TIMESTAMP_VARIABLE_NAME_CONFIG
                                             |  $TIMESTAMP_FIELD_NAME_CONFIG""".stripMargin
      case IncrementingMode           => s"""Required connector properties:
                                             |  $INCREMENTING_VARIABLE_NAME_CONFIG
                                             |  $INCREMENTING_FIELD_NAME_CONFIG""".stripMargin
      case TimestampIncrementingMode  => s"""Required connector properties:
                                             |  $TIMESTAMP_VARIABLE_NAME_CONFIG
                                             |  $TIMESTAMP_FIELD_NAME_CONFIG
                                             |  $INCREMENTING_VARIABLE_NAME_CONFIG
                                             |  $INCREMENTING_FIELD_NAME_CONFIG""".stripMargin
    }
  )
  */

/**
  * @return database connection url
  */
  def getHttpUrl: String = connectorProperties(HTTP_URL_CONFIG)

/**
  * @return mode of operation [[IncrementingMode]], [[TimestampMode]], [[TimestampIncrementingMode]]
  */
  //def getMode: Mode = Mode.withName(connectorProperties(MODE_CONFIG))

/**
  * @return stored procedure name
  */
  //def getStoredProcedureName: String = connectorProperties(STORED_PROCEDURE_NAME_CONFIG)

  /**
    * @return kafka topic name
    */
  def getTopic: String = connectorProperties(TOPIC_CONFIG)

  /**
    * @return service name
    */
  def getService: String = connectorProperties(SERVICE_CONFIG)

/**
  * @return database poll interval
  */
  def getPollInterval: Long = connectorProperties.getOrElse(POLL_INTERVAL_MS_CONFIG, POLL_INTERVAL_MS_DEFAULT).toLong

/**
  * @return number of records fetched in each poll
  */
  //def getMaxBatchSize: Int = connectorProperties.getOrElse(BATCH_MAX_ROWS_CONFIG, BATCH_MAX_ROWS_DEFAULT).toInt

/**
  * @return batch size variable name in stored procedure
  */
  //def getMaxBatchSizeVariableName: String = connectorProperties(BATCH_MAX_ROWS_VARIABLE_NAME_CONFIG)

/**
  * @return timestamp offset variable name in stored procedure
  */
  //def getTimestampVariableName: Option[String] = connectorProperties.get(TIMESTAMP_VARIABLE_NAME_CONFIG)

/**
  * @return timestamp offset field name in record
  */
  //def getTimestampFieldName: Option[String] = connectorProperties.get(TIMESTAMP_FIELD_NAME_CONFIG)

/**
  * @return incrementing offset variable name in stored procedure
  */
  //def getIncrementingVariableName: Option[String] = connectorProperties.get(INCREMENTING_VARIABLE_NAME_CONFIG)

/**
  * @return incrementing offset field name in record
  */
  //def getIncrementingFieldName: Option[String] = connectorProperties.get(INCREMENTING_FIELD_NAME_CONFIG)

/**
  * @return initial timestamp offset
  */
/*
  def getTimestampOffset: Long = {
    connectorProperties
      .get(TIMESTAMP_OFFSET_CONFIG)
      .map(o => new Timestamp(Timestamp.valueOf(o).getTime + TimeZone.getDefault.getRawOffset))
      .getOrElse(TIMESTAMP_OFFSET_DEFAULT).getTime
  }
*/

/**
  * @return initial incrementing offset
  */
//  def getIncrementingOffset: Long = connectorProperties.getOrElse(INCREMENTING_OFFSET_CONFIG, INCREMENTING_OFFSET_DEFAULT).toLong

/**
  * @return optional field name to be used as kafka message key
  */
//  def getKeyField: Option[String] = connectorProperties.get(KEY_FIELD_NAME_CONFIG)
}