package com.veluxer.occupying.domain.srt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.Result

@JsonIgnoreProperties(ignoreUnknown = true)
data class SrtReservationResult(
    @JsonProperty("resultMap")
    private val resultList: List<SrtReservationResultData>,
) : Result {
    private val successCode = "SUCC"
    private val resultData = resultList.first()

    override fun isSuccess(): Boolean = resultData.result == successCode
    override fun getMessage(): String = resultData.message
}
