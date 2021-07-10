package com.veluxer.occupying.domain

import java.time.ZonedDateTime

interface Train {
    fun getNumber(): String
    fun reservable(): Boolean
    fun getDepartureStation(): Station
    fun getDepartureDateTime(): ZonedDateTime
    fun getDestinationStation(): Station
}
