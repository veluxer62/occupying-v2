package com.veluxer.occupying.domain.korail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailSearchResponseBody(
    @JsonProperty("trn_infos")
    val trainInformation: KorailTrainInformation,
)
