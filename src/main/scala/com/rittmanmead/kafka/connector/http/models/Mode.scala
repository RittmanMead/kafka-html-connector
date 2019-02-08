package com.rittmanmead.kafka.connector.http.models

import enumeratum._

import scala.collection.immutable.IndexedSeq

/**
  * Mode of operation
  * ~~~~~~~~~~~~~~~~~
  *
  * Timestamp Mode                :: creation timestamp of a record is stored as offset.
  *
  * Incrementing Mode             :: unique (auto) incrementing integral id of a record is stored as offset.
  *
  * Timestamp + Incrementing Mode :: pair of creation timestamp and unique (auto) incrementing integral id of a record
  *                                  is stored as offset.
  */
sealed abstract class Mode(override val entryName: String) extends EnumEntry

object Mode extends Enum[Mode] {

  val values: IndexedSeq[Mode] = findValues

  case object TimestampMode extends Mode("timestamp")
  case object IncrementingMode extends Mode("incrementing")
  case object TimestampIncrementingMode extends Mode("timestamp+incrementing")
}
