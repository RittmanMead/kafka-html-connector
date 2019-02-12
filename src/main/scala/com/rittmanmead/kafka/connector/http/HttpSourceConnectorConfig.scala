package com.rittmanmead.kafka.connector.http


import com.rittmanmead.kafka.connector.http.HttpSourceConnectorConstants._

/**
  * @constructor
  * @param connectorProperties is set of configurations required to create JdbcSourceConnectorConfig
  */
class HttpSourceConnectorConfig(val connectorProperties: Map[String, String]) {

  // validate connector properties being populated
  require(
    connectorProperties.contains(HTTP_URL_CONFIG) &&
    connectorProperties.contains(API_KEY_CONFIG) &&
    connectorProperties.contains(API_PARAMS_CONFIG) &&
    connectorProperties.contains(SERVICE_CONFIG) &&
    connectorProperties.contains(TOPIC_CONFIG) &&
    connectorProperties.contains(TASKS_MAX_CONFIG) &&
    connectorProperties.contains(CONNECTOR_CLASS) &&
    connectorProperties.contains(POLL_INTERVAL_MS_CONFIG),

    s"""Missing properties for Http Source Connector. Required:
       | $HTTP_URL_CONFIG
       | $API_KEY_CONFIG
       | $API_PARAMS_CONFIG
       | $SERVICE_CONFIG
       | $TOPIC_CONFIG
       | $TASKS_MAX_CONFIG
       | $CONNECTOR_CLASS
       | $POLL_INTERVAL_MS_CONFIG""".stripMargin
  )

  // validate API params format

  def paramMap(paramsString: String): Either[String, Map[String, String]] = {
    val paramArray = paramsString.split("&")

    val paramsSplit: List[Either[String, (String, String)]] = paramArray map (param => param.split("=") match {
      case Array(paramKey, paramVal) => Right(paramKey -> paramVal)
      case _ => Left(param)
    }) toList

    if ( paramsSplit.find(_.isLeft).isDefined )
      Left( paramsSplit.find(_.isLeft).get match { case(Left(a)) => a } )
    else
      Right( paramsSplit map {case(Right(a) ) => a} toMap )
  }

  lazy val paramMapParsed: Either[String, Map[String, String]] = paramMap( connectorProperties(API_PARAMS_CONFIG) )

  require(
    paramMapParsed.isRight,
    s"API parameter map $API_PARAMS_CONFIG is not in the required format 'key1=value1&key2=value2&key3=value3,...'. At least one key=value pair is required."
  )


  /**
  * @return database connecti
    url
  */
  def getApiHttpUrl: String = connectorProperties(HTTP_URL_CONFIG)

  /**
    * @return API key
    */
  def getApiKey: String = connectorProperties(API_KEY_CONFIG)

  /**
    * @return additional API parameters
    */
  def getApiParams: Map[String, String] = paramMapParsed match {
    case Right(params) => params
    case _ => Map()
  }

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