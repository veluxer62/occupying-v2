package com.veluxer.occupying.domain

enum class SeatType(val label: String, val code: String) {
    NORMAL("일반", "000"),
    WINDOW_SEAT("창가", "012"),
    AISLE_SEAT("복도", "013"),
}

