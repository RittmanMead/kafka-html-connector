package com.rittmanmead.kafka.connector.http

import java.util.{List => JavaList, Map => JavaMap}
import java.util.concurrent.atomic.{AtomicBoolean => JavaBoolean}

import com.rittmanmead.kafka.connector.http.utils.{DataConverter, Version}
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.source.{SourceRecord, SourceTask}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class HttpSourceTask extends SourceTask {
  private val taskLogger: Logger = LoggerFactory.getLogger(classOf[HttpSourceTask])
  private var taskConfig: HttpSourceTaskConfig  = _

  //private val dataConverter = new DataConverter

  private var httpService: HttpService    = _
  private var running: JavaBoolean        = _

  override def version(): String = Version.getVersion

  /**
    * invoked by kafka-connect runtime to start this task
    *
    * @param connectorProperties properties required to start this task
    */
  override def start(connectorProperties: JavaMap[String, String]): Unit = {
    Try(new HttpSourceTaskConfig(connectorProperties.asScala.toMap)) match {
      case Success(cfg) => taskConfig = cfg
      case Failure(err) => taskLogger.error(s"Could not start Task ${this.getClass.getName} due to error in configuration.", new ConnectException(err))
    }

    val httpUrl: String = taskConfig.getHttpUrl
    val pollInterval: Long = taskConfig.getPollInterval

    taskLogger.info(s"Setting up Http service for ${httpUrl}...")
    Try( new HttpService(taskConfig.getTopic, taskConfig.getService, httpUrl, taskLogger) ) match {
      case Success(service) =>  httpService = service
      case Failure(error) =>    taskLogger.error(s"Could not establish a Http service to ${httpUrl}")
                                throw error
    }

    taskLogger.info(s"Starting to fetch from ${httpUrl} each ${pollInterval}ms...")
    running = new JavaBoolean(true)
  }

  /**
    * invoked by kafka-connect runtime to stop this task
    */
  override def stop(): Unit = {
    if (running != null) {
      taskLogger.info("Stopping task.")
      running.set(false)
    }
  }

  /**
    * invoked by kafka-connect runtime to poll data in [[HttpSourceConnectorConstants.POLL_INTERVAL_MS_CONFIG]] interval
    */
  override def poll(): JavaList[SourceRecord] = this.synchronized { if(running.get) fetchRecords else null }

  private def fetchRecords: JavaList[SourceRecord] = {
    taskLogger.debug("Polling new data...")

    val pollInterval = taskConfig.getPollInterval
    val startTime    = System.currentTimeMillis

    val fetchedRecords: Seq[SourceRecord] = Try( httpService.getWeatherRecords() ) match {
      case Success(records)                    => if(records.isEmpty) taskLogger.info(s"No data from ${taskConfig.getService}")
                                                  else taskLogger.info(s"Got ${records.size} results for ${taskConfig.getService}")
                                                  records

      case Failure(error: Throwable)           => taskLogger.error(s"Failed to fetch data for ${taskConfig.getService}: ", error)
                                                  Seq.empty[SourceRecord]
    }

    val endTime     = System.currentTimeMillis
    val elapsedTime = endTime - startTime

    if(elapsedTime < pollInterval) Thread.sleep(pollInterval - elapsedTime)

    fetchedRecords.asJava
  }

}
