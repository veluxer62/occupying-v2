package com.veluxer.occupying.domain.korail

import com.fasterxml.jackson.annotation.JsonProperty

data class KorailTrainInformation(
    @JsonProperty("trn_info")
    val trains: List<KorailTrain>,
)
