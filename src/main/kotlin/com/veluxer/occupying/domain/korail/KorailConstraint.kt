package com.veluxer.occupying.domain.korail

import java.time.format.DateTimeFormatter

object KorailConstraint {
    const val LOGIN_PATH = "/classes/com.korail.mobile.login.Login"
    const val SEARCH_PATH = "/classes/com.korail.mobile.seatMovie.ScheduleView"
    const val RESERVATION_PATH = "/classes/com.korail.mobile.certification.TicketReservation"
    val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss")
    const val SESSION_COOKIE_NAME = "JSESSIONID"
}
