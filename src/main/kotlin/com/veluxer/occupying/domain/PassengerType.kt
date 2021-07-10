package com.veluxer.occupying.domain

enum class PassengerType(val label: String, val code: String) {
    ADULT_YOUTH("어른/청소년", "1"),
    CHILD("어린이", "5"),
    SENIOR("경로", "4"),
    DISABLED_SEVERE("장애 1~3급", "2"),
    DISABLED_NOT_SEVERE("장애 4~6급", "3"),
}
