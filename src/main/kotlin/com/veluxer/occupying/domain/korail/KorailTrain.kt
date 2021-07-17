package com.veluxer.occupying.domain.korail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.SeatStatus
import com.veluxer.occupying.domain.Station
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.TrainType
import com.veluxer.occupying.domain.korail.KorailConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.korail.KorailConstraint.TIME_FORMAT
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailTrain(
    @JsonProperty("h_trn_no")
    private val number: String,
    @JsonProperty("h_trn_clsf_cd")
    private val trainType: String,
    @JsonProperty("h_gen_rsv_cd")
    private val seatStatus: String,
    @JsonProperty("h_rcvd_amt")
    private val fare: String,
    @JsonProperty("h_dpt_dt")
    private val departureDate: String,
    @JsonProperty("h_dpt_tm")
    private val departureTime: String,
    @JsonProperty("h_dpt_rs_stn_cd")
    private val departureStation: String,
    @JsonProperty("h_arv_dt")
    private val arrivalDate: String,
    @JsonProperty("h_arv_tm")
    private val arrivalTime: String,
    @JsonProperty("h_arv_rs_stn_cd")
    private val destinationStation: String,
) : Train {
    override fun getNumber(): String = number

    override fun getTrainType(): TrainType = TrainType.values().first { it.code == trainType }

    override fun getSeatStatus(): SeatStatus = SeatStatus.values().first { it.code.contains(seatStatus) }

    override fun getFare(): Int = fare.toInt()

    override fun getDepartureDateTime(): ZonedDateTime = ZonedDateTime.of(
        LocalDate.parse(departureDate, DATE_FORMAT),
        LocalTime.parse(departureTime, TIME_FORMAT),
        ZoneId.systemDefault()
    )

    override fun getDepartureStation(): Station = Station.values().first { it.code == departureStation }

    override fun getArrivalDateTime(): ZonedDateTime = ZonedDateTime.of(
        LocalDate.parse(arrivalDate, DATE_FORMAT),
        LocalTime.parse(arrivalTime, TIME_FORMAT),
        ZoneId.systemDefault()
    )

    override fun getDestinationStation(): Station = Station.values().first { it.code == destinationStation }
}
