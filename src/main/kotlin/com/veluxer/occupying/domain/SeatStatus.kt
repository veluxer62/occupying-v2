package com.veluxer.occupying.domain

enum class SeatStatus(val code: List<String>, val label: String) {
    AVAILABLE(listOf("11", "YY"), "예약가능"),
    SOLD_OUT(listOf("00", "13", "NN"), "매진")
}
