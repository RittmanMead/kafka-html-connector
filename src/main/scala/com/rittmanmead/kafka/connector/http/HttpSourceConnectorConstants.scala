package com.rittmanmead.kafka.connector.http

object HttpSourceConnectorConstants {
  val HTTP_URL_CONFIG               = "http.url"
  val SERVICE_CONFIG                = "service.name"
  val TOPIC_CONFIG                  = "topic"
  val TASKS_MAX_CONFIG              = "tasks.max"
  val CONNECTOR_CLASS               = "connector.class"

  val POLL_INTERVAL_MS_CONFIG       = "poll.interval.ms"
  val POLL_INTERVAL_MS_DEFAULT      = "5000"
}
