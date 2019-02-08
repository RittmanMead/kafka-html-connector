package com.rittmanmead.kafka.connector.http

import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.source.SourceRecord
import org.slf4j.Logger
import scalaj.http.{Http, HttpResponse}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer


// todo: query and appid - as separate params? less universal
class HttpService(topic: String, /*schema: Schema,*/ serviceName: String, apiUrl: String, taskLogger: Logger) {

    def getWeatherRecords(): Seq[SourceRecord] = {
        val sourceRecords = ListBuffer.empty[SourceRecord]
        val weatherResult = httpRequest
        taskLogger.info(s"Http return code: ${weatherResult.code}")

        sourceRecords += new SourceRecord(
            Map(HttpSourceConnectorConstants.SERVICE_CONFIG -> serviceName).asJava,
            Map("offset" -> "n/a").asJava,
            topic,
            Schema.STRING_SCHEMA /*schema*/,
            weatherResult.body
        )

        sourceRecords
    }

    private def httpRequest(): HttpResponse[String] = {
        //Http("http://api.openweathermap.org/data/2.5/weather").param("q", "London,UK").param("APPID", "63bcda0bc79f1c420b23cbf4f6d55076").asString
        Http(apiUrl).asString
    }
}
