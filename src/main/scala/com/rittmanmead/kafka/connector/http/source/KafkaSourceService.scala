package com.rittmanmead.kafka.connector.http.source

import com.rittmanmead.kafka.connector.http.schema.KafkaSchemaParser
import org.apache.kafka.connect.source.SourceRecord

trait KafkaSourceService[SchemaInputType, SchemaOutputType] {
    def sourceRecords: Seq[SourceRecord]
    val topic: String
    val schemaParser: KafkaSchemaParser[SchemaInputType, SchemaOutputType]
}
