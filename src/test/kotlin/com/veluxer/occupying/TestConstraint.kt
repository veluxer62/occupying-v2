package com.veluxer.occupying

import com.veluxer.occupying.domain.Station
import java.time.ZoneId
import java.time.ZonedDateTime

internal object TestConstraint {
    const val MOCK_SERVER_HOST = "localhost"
    const val MOCK_SERVER_PORT = 1080
    const val LOGIN_ID = "test-id"
    const val SUCCESS_LOGIN_PW = "test-pw"
    const val FAILURE_LOGIN_PW = "failure-pw"
    const val JSESSIONID = "UXSox5cYRAEd1RjeG5Od0wMPAYiqKtpgTkkfI5la2kbmaITtJ4sYVTU7G4yoGkqa"
    val SEARCH_DEPARTURE_DATETIME: ZonedDateTime = ZonedDateTime.of(2021, 7, 1, 7, 0, 0, 0, ZoneId.systemDefault())
    val SEARCH_DEPARTURE_STATION = Station.SEOUL
    val SEARCH_DESTINATION_STATION = Station.BUSAN
}
