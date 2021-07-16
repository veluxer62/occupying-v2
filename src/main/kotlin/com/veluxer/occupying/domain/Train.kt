package com.veluxer.occupying.domain

import java.time.ZonedDateTime

interface Train {
    fun getNumber(): String
    fun getTrainType(): TrainType
    fun getSeatStatus(): SeatStatus
    fun getFare(): Int
    fun getDepartureDateTime(): ZonedDateTime
    fun getDepartureStation(): Station
    fun getArrivalDateTime(): ZonedDateTime
    fun getDestinationStation(): Station
}
