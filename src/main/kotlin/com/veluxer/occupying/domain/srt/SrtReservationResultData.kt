package com.veluxer.occupying.domain.srt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SrtReservationResultData(
    @JsonProperty("strResult")
    val result: String,
    @JsonProperty("msgTxt")
    val message: String,
)
