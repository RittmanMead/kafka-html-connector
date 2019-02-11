package com.rittmanmead.kafka.connector.http.schema

import org.apache.kafka.connect.data.{Schema, Struct}

trait KafkaSchemaParser[InputType, OutputType] {
    val schema: Schema
    def output(input: InputType): OutputType
}
