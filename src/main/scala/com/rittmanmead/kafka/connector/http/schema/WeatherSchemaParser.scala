package com.rittmanmead.kafka.connector.http.schema

import com.rittmanmead.kafka.connector.http.HttpSourceTask
import io.circe.Decoder
import io.circe.parser.decode

import io.circe.generic.auto._
//import io.circe.parser._

import org.apache.kafka.connect.data.{Schema, SchemaBuilder, Struct}
import org.slf4j.{Logger, LoggerFactory}

object WeatherSchemaParser extends KafkaSchemaParser[String, Struct] {

    private val logger: Logger = LoggerFactory.getLogger(classOf[HttpSourceTask])

    override val schema: Schema = SchemaBuilder.struct().name("weatherSchema")
        .field("coord-lon", Schema.FLOAT64_SCHEMA)
        .field("coord-lat", Schema.FLOAT64_SCHEMA)

        .field("weather-id", Schema.FLOAT64_SCHEMA)
        .field("weather-main", Schema.STRING_SCHEMA)
        .field("weather-description", Schema.STRING_SCHEMA)
        .field("weather-icon", Schema.STRING_SCHEMA)

        .field("base", Schema.STRING_SCHEMA)

        .field("main-temp", Schema.FLOAT64_SCHEMA)
        .field("main-pressure", Schema.FLOAT64_SCHEMA)
        .field("main-humidity", Schema.FLOAT64_SCHEMA)
        .field("main-temp-min", Schema.FLOAT64_SCHEMA)
        .field("main-temp-max", Schema.FLOAT64_SCHEMA)

        .field("visibility", Schema.FLOAT64_SCHEMA)

        .field("wind-speed", Schema.FLOAT64_SCHEMA)
        .field("wind-deg", Schema.FLOAT64_SCHEMA)

        //.field("rain", Schema.FLOAT64_SCHEMA)

        .field("clouds-all", Schema.FLOAT64_SCHEMA)

        .field("dt", Schema.FLOAT64_SCHEMA)

        .field("sys-type", Schema.FLOAT64_SCHEMA)
        .field("sys-id", Schema.FLOAT64_SCHEMA)
        .field("sys-message", Schema.FLOAT64_SCHEMA)
        .field("sys-country", Schema.STRING_SCHEMA)
        .field("sys-sunrise", Schema.FLOAT64_SCHEMA)
        .field("sys-sunset", Schema.FLOAT64_SCHEMA)

        .field("id", Schema.FLOAT64_SCHEMA)
        .field("name", Schema.STRING_SCHEMA)
        .field("cod", Schema.FLOAT64_SCHEMA)

        .build()


    private val emptyString: String = "N/A" // todo: optional data type???
    private val emptyNumber: Double = -1 // todo: optional data tyoe???

    case class Coord(lon: Double, lat: Double)
    case class WeatherAtom(id: Double, main: String, description: String, icon: String)
    case class Main(temp: Double, pressure: Double, humidity: Double, temp_min: Double, temp_max: Double)

    case class Wind(speed: Double, deg: Double)
    object Wind {
        implicit val decoder: Decoder[Wind] = Decoder.instance { h =>
            for {
                speed <- h.getOrElse[Double]("speed")(emptyNumber) // todo: optional schema type!!!!!
                deg <- h.getOrElse[Double]("deg")(emptyNumber)
            } yield Wind(
                speed,
                deg
            )
        }
    }

    case class Rain(threeH: Double)
    object Rain {
        implicit val decoder: Decoder[Rain] = Decoder.instance { h =>
            for {
                threeH <- h.get[Double]("3h")
            } yield Rain(
                threeH
            )
        }
    }

    case class Clouds(all: Double)

    case class Sys(sysType: Double, id: Double, message: Double, country: String, sunrise: Double, sunset: Double)
    object Sys {
        implicit val decoder: Decoder[Sys] = Decoder.instance { h =>
            for {
                sysType <- h.get[Double]("type")
                id <- h.get[Double]("id")
                message <- h.get[Double]("message")
                country <- h.get[String]("country")
                sunrise <- h.get[Double]("sunrise")
                sunset <- h.get[Double]("sunset")
            } yield Sys(
                sysType,
                id,
                message,
                country,
                sunrise,
                sunset
            )
        }
    }

    val emptyCoord = Coord(emptyNumber, emptyNumber)
    val emptySys = Sys(emptyNumber, emptyNumber, emptyNumber, emptyString, emptyNumber, emptyNumber)
    val emptyWeatherAtom = WeatherAtom(emptyNumber, emptyString, emptyString, emptyString)
    val emptyMain = Main(emptyNumber, emptyNumber, emptyNumber, emptyNumber, emptyNumber)
    val emptyWind = Wind(emptyNumber, emptyNumber)
    val emptyRain = Rain(emptyNumber)
    val emptyClouds = Clouds(emptyNumber)


    case class WeatherSchema(
                                coord: Coord,
                                weather: List[WeatherAtom],
                                base: String,
                                mainVal: Main,
                                visibility: Double,
                                wind: Wind,
                                //rain: Rain,
                                clouds: Clouds,
                                dt: Double,
                                sys: Sys,
                                id: Double,
                                name: String,
                                cod: Double
                            )

    object WeatherSchema {

        implicit val decoder: Decoder[WeatherSchema] = Decoder.instance { h =>

            for {
                coord <- h.get[Coord]("coord")
                weather <- h.get[List[WeatherAtom]]("weather")
                base <- h.get[String]("base")
                main <- h.get[Main]("main")
                visibility <- h.get[Double]("visibility")
                wind <- h.get[Wind]("wind")
                clouds <- h.get[Clouds]("clouds")
                dt <- h.get[Double]("dt")
                sys <- h.get[Sys]("sys")
                id <- h.get[Double]("id")
                name <- h.get[String]("name")
                cod <- h.get[Double]("cod")

            } yield WeatherSchema(
                coord,
                weather,
                base,
                main,
                visibility,
                wind,
                clouds,
                dt,
                sys,
                id,
                name,
                cod
            )

        }

    }

    val emptyWeatherSchema: WeatherSchema = WeatherSchema(
        emptyCoord,
        List(),
        emptyString,
        emptyMain,
        emptyNumber,
        emptyWind,
        //emptyRain,
        emptyClouds,
        emptyNumber,
        emptySys,
        emptyNumber,
        emptyString,
        emptyNumber
    )


    override def output(structInput: String): Struct = {

        logger.info(s"Weather Schema parser: JSON text to be parsed: ${structInput}")

        val weatherParsed: WeatherSchema = decode[WeatherSchema](structInput) match {
            case Left(error) => {
                logger.error(s"JSON parser error: ${error}")
                emptyWeatherSchema
            }
            case Right(weather) => weather
        }

        logger.info(s"JSON parsed class content: ${weatherParsed}")


        val weatherStruct: Struct = new Struct(schema)
            .put("coord-lon", weatherParsed.coord.lon)
            .put("coord-lat", weatherParsed.coord.lat)

            .put("weather-id", weatherParsed.weather.headOption.getOrElse(emptyWeatherAtom).id)
            .put("weather-main", weatherParsed.weather.headOption.getOrElse(emptyWeatherAtom).main)
            .put("weather-description", weatherParsed.weather.headOption.getOrElse(emptyWeatherAtom).description)
            .put("weather-icon", weatherParsed.weather.headOption.getOrElse(emptyWeatherAtom).icon)

            .put("base", weatherParsed.base)

            .put("main-temp", weatherParsed.mainVal.temp)
            .put("main-pressure", weatherParsed.mainVal.pressure)
            .put("main-humidity", weatherParsed.mainVal.humidity)
            .put("main-temp-min", weatherParsed.mainVal.temp_min)
            .put("main-temp-max", weatherParsed.mainVal.temp_max)

            .put("visibility", weatherParsed.visibility)

            .put("wind-speed", weatherParsed.wind.speed)
            .put("wind-deg", weatherParsed.wind.deg)

            //.put("rain", weatherParsed.rain.threeH)

            .put("clouds-all", weatherParsed.clouds.all)

            .put("dt", weatherParsed.dt)

            .put("sys-type", weatherParsed.sys.sysType)
            .put("sys-id", weatherParsed.sys.id)
            .put("sys-message", weatherParsed.sys.message)
            .put("sys-country", weatherParsed.sys.country)
            .put("sys-sunrise", weatherParsed.sys.sunrise)
            .put("sys-sunset", weatherParsed.sys.sunset)


            .put("id", weatherParsed.id)
            .put("name", weatherParsed.name)
            .put("cod", weatherParsed.cod)

        weatherStruct
    }

}
