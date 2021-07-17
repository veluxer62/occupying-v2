package com.veluxer.occupying.domain.korail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailLoginResponseBody(
    @JsonProperty("h_msg_txt")
    val message: String,
    @JsonProperty("h_msg_cd")
    val code: String,
)
