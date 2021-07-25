package com.veluxer.occupying.domain.srt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.SeatStatus
import com.veluxer.occupying.domain.Station
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.TrainType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class SrtTrain(
    @JsonProperty("trnNo")
    private val number: String,
    @JsonProperty("stlbTrnClsfCd")
    private val trainType: String,
    @JsonProperty("stmpRsvPsbFlgCd")
    private val seatStatus: String,
    @JsonProperty("rcvdAmt")
    private val fare: String,
    @JsonProperty("dptDt")
    private val departureDate: String,
    @JsonProperty("dptTm")
    private val departureTime: String,
    @JsonProperty("dptRsStnCd")
    private val departureStation: String,
    @JsonProperty("arvDt")
    private val arrivalDate: String,
    @JsonProperty("arvTm")
    private val arrivalTime: String,
    @JsonProperty("arvRsStnCd")
    private val destinationStation: String,
) : Train {
    override fun getNumber(): String = number
    override fun getTrainType(): TrainType = TrainType.values().first { it.code == trainType }
    override fun getSeatStatus(): SeatStatus = SeatStatus.values().first { it.code.contains(seatStatus) }
    override fun getFare(): Int = fare.toInt()
    override fun getDepartureDateTime(): ZonedDateTime = ZonedDateTime.of(
        LocalDate.parse(departureDate, SrtConstraint.DATE_FORMAT),
        LocalTime.parse(departureTime, SrtConstraint.TIME_FORMAT),
        ZoneId.systemDefault()
    )

    override fun getDepartureStation(): Station = Station.values().first { it.code == departureStation }
    override fun getArrivalDateTime(): ZonedDateTime = ZonedDateTime.of(
        LocalDate.parse(arrivalDate, SrtConstraint.DATE_FORMAT),
        LocalTime.parse(arrivalTime, SrtConstraint.TIME_FORMAT),
        ZoneId.systemDefault()
    )

    override fun getDestinationStation(): Station = Station.values().first { it.code == destinationStation }
}
