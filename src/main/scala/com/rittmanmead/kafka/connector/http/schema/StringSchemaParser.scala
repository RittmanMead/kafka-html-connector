package com.rittmanmead.kafka.connector.http.schema

import org.apache.kafka.connect.data.Schema

object StringSchemaParser extends KafkaSchemaParser[String, String] {
    override val schema: Schema = Schema.STRING_SCHEMA
    override def output(inputString: String) = inputString
}
