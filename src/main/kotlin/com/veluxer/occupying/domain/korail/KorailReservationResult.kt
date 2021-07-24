package com.veluxer.occupying.domain.korail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.Result

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailReservationResult(
    @JsonProperty("strResult")
    private val result: String,
    @JsonProperty("h_msg_txt")
    private val message: String,
) : Result {
    private val successCode = "SUCC"

    override fun isSuccess(): Boolean = result == successCode
    override fun getMessage(): String = message
}
