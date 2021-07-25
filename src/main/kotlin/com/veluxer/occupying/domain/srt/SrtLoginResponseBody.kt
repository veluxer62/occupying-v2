package com.veluxer.occupying.domain.srt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SrtLoginResponseBody(
    @JsonProperty("MSG")
    val message: String?,
    @JsonProperty("strResult")
    val code: String?,
)
