package com.veluxer.occupying.domain.srt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SrtSearchResponseBody(
    @JsonProperty("trainListMap")
    val trains: List<SrtTrain>,
)
