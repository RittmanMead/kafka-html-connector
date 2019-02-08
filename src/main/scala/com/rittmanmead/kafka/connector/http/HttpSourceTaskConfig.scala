package com.rittmanmead.kafka.connector.http

/**
  * @constructor
  * @param properties is set of configurations required to create JdbcSourceTaskConfig
  */
class HttpSourceTaskConfig(properties: Map[String, String]) extends HttpSourceConnectorConfig(properties)