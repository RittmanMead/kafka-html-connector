package com.rittmanmead.kafka.connector.http

import java.sql.{Connection, DriverManager, SQLException}
import java.util
import java.util.concurrent.atomic.AtomicBoolean

import com.rittmanmead.kafka.connector.http.models.Mode.{IncrementingMode, TimestampIncrementingMode, TimestampMode}
import com.rittmanmead.kafka.connector.http.services.HttpService
//import com.rittmanmead.kafka.connector.http.services.{IdBasedDataService, TimeBasedDataService, TimeIdBasedDataService}
import com.rittmanmead.kafka.connector.http.models.DatabaseProduct
import com.rittmanmead.kafka.connector.http.services.DataService
import com.rittmanmead.kafka.connector.http.utils.{DataConverter, Version}
import org.apache.kafka.connect.errors.ConnectException
import org.apache.kafka.connect.source.{SourceRecord, SourceTask}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class HttpSourceTask extends SourceTask {
  private val logger = LoggerFactory.getLogger(classOf[HttpSourceTask])
  private val dataConverter = new DataConverter

  private var config: HttpSourceTaskConfig  = _
  //private var db: Connection                = _
  //private var dataService: DataService      = _

  private var httpService: HttpService      = _
  private var running: AtomicBoolean        = _ // todo: change this to Scala Boolean

  override def version(): String = Version.getVersion

/**
  * invoked by kafka-connect runtime to start this task
  *
  * @param connectorProperties properties required to start this task
  */
  override def start(connectorProperties: util.Map[String, String]): Unit = {
    Try(new HttpSourceTaskConfig(connectorProperties.asScala.toMap)) match {
      case Success(conf) => config = conf
      case Failure(error) => logger.error(s"Couldn't start ${this.getClass.getName} due to configuration error", new ConnectException(error))
    }

    val httpUrl = config.getHttpUrl
    val pollInterval = config.getPollInterval

    logger.info(s"Starting to fetch from ${httpUrl} each ${pollInterval} ms")



    /*
    logger.debug(s"Trying to connect to $httpUrl")
    Try(DriverManager.getConnection(httpUrl)) match {
      case Success(c)               => db = c
      case Failure(e: SQLException) => logger.error(s"Couldn't open connection to $httpUrl : ", e)
                                       throw new ConnectException(e)
      case Failure(e)               => logger.error(s"Couldn't open connection to $httpUrl : ", e)
                                       throw e
    }
    */

    //val databaseProduct =  DatabaseProduct.withName(db.getMetaData.getDatabaseProductName)

    /*
    val offset = context.offsetStorageReader().offset(
      Map(HttpSourceConnectorConstants.STORED_PROCEDURE_NAME_KEY -> config.getStoredProcedureName).asJava
    )

    val storedProcedureName         = config.getStoredProcedureName
    val timestampVariableNameOpt    = config.getTimestampVariableName
    val timestampFieldNameOpt       = config.getTimestampFieldName
    val incrementingVariableNameOpt = config.getIncrementingVariableName
    val incrementingFieldNameOpt    = config.getIncrementingFieldName
    val batchSize                   = config.getMaxBatchSize
    val batchSizeVariableName       = config.getMaxBatchSizeVariableName
    val topic                       = config.getTopic
    val keyFieldOpt                 = config.getKeyField

    config.getMode match {
      case TimestampMode =>
        val timestampOffset = Try(offset.get(TimestampMode.entryName)).map(_.toString.toLong).getOrElse(config.getTimestampOffset)
        dataService = TimeBasedDataService(databaseProduct, storedProcedureName, batchSize, batchSizeVariableName,
            timestampVariableNameOpt.get, timestampOffset, timestampFieldNameOpt.get, topic, keyFieldOpt, dataConverter)

      case IncrementingMode =>
        val incrementingOffset = Try(offset.get(IncrementingMode.entryName)).map(_.toString.toLong).getOrElse(config.getIncrementingOffset)
        dataService = IdBasedDataService(databaseProduct, storedProcedureName, batchSize, batchSizeVariableName,
            incrementingVariableNameOpt.get, incrementingOffset, incrementingFieldNameOpt.get, topic, keyFieldOpt, dataConverter)

      case TimestampIncrementingMode =>
        val timestampOffset    = Try(offset.get(TimestampMode.entryName)).map(_.toString.toLong).getOrElse(config.getTimestampOffset)
        val incrementingOffset = Try(offset.get(IncrementingMode.entryName)).map(_.toString.toLong).getOrElse(config.getIncrementingOffset)
        dataService = TimeIdBasedDataService(databaseProduct, storedProcedureName, batchSize, batchSizeVariableName,
            timestampVariableNameOpt.get, timestampOffset, incrementingVariableNameOpt.get, incrementingOffset,
            timestampFieldNameOpt.get, incrementingFieldNameOpt.get, topic, keyFieldOpt, dataConverter)
    }

    */

    httpService = new HttpService(config.getTopic, config.getService)
    //  timestampVariableNameOpt.get, timestampOffset, timestampFieldNameOpt.get, topic, keyFieldOpt, dataConverter)


    running = new AtomicBoolean(true)
  }

/**
  * invoked by kafka-connect runtime to stop this task
  */
  override def stop(): Unit = {

    if (running != null) {
      logger.debug("Stopping task.")
      running.set(false)
    }

    /*
    if (db != null) {
      logger.debug("Trying to close database connection")
      Try(db.close()) match {
        case Success(_) =>
        case Failure(e) => logger.error("Failed to close database connection: ", e)
      }
    }
    */

  }

/**
  * invoked by kafka-connect runtime to poll data in [[HttpSourceConnectorConstants.POLL_INTERVAL_MS_CONFIG]] interval
  */
  override def poll(): util.List[SourceRecord] = this.synchronized { if(running.get) fetchRecords else null }

  private def fetchRecords: util.List[SourceRecord] = {
    logger.debug("Polling new data ...")

    val pollInterval = config.getPollInterval
    val startTime    = System.currentTimeMillis

    val fetchedRecords: Seq[SourceRecord] = httpService.getWeatherRecords() /*dataService.getRecords(db, pollInterval.millis)*/ match {
      case Success(records)                    => if(records.isEmpty) logger.info(s"No data for from ${config.getService}")
                                                  else logger.info(s"Returning ${records.size} records for ${config.getService}")
                                                  records
      //case Failure(e: SQLException)            => logger.error(s"Failed to fetch data for ${config.getService}: ", e)
      //                                            Seq.empty[SourceRecord]
      case Failure(e: Throwable)               => logger.error(s"Failed to fetch data for ${config.getService}: ", e)
                                                  Seq.empty[SourceRecord]
    }

    val endTime     = System.currentTimeMillis
    val elapsedTime = endTime - startTime

    if(elapsedTime < pollInterval) Thread.sleep(pollInterval - elapsedTime)

    fetchedRecords.asJava

    //Seq().asJava
  }

}
