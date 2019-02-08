package com.rittmanmead.kafka.connector.http.services

import com.rittmanmead.kafka.connector.http.HttpSourceConnectorConstants

import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.data.Schema

import scalaj.http.{Http, HttpResponse}

import scala.collection.JavaConverters._

import scala.collection.mutable.ListBuffer
import scala.util.Try


// todo: query and appid - as separate params? less universal
class HttpService(topic: String, /*schema: Schema,*/ serviceName: String) {


    def getWeatherRecords(): Try[Seq[SourceRecord]] = Try {
        val sourceRecords = ListBuffer.empty[SourceRecord]
        val weatherString: String = getWeather

        sourceRecords += new SourceRecord(
            Map(HttpSourceConnectorConstants.SERVICE_CONFIG -> serviceName).asJava,
            Map("offset" -> "n/a").asJava,
            topic,
            Schema.STRING_SCHEMA /*schema*/,
            weatherString
        )

        sourceRecords
    }


    private def getWeather(): String = {

        val result: HttpResponse[String] = Http("http://api.openweathermap.org/data/2.5/weather").param("q", "London,UK").param("APPID", "63bcda0bc79f1c420b23cbf4f6d55076").asString
        //val result: HttpResponse[String] = Http("api.openweathermap.org/data/2.5/weather").param("q", "London,uk").asString

        //println(result)

        result.body // todo: body, code, etc HANDLE ERRORS!
    }

}
