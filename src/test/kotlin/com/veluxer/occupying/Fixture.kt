package com.veluxer.occupying

import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.SeatStatus
import com.veluxer.occupying.domain.Station
import com.veluxer.occupying.domain.TrainType
import com.veluxer.occupying.domain.korail.KorailConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.korail.KorailConstraint.TIME_FORMAT
import com.veluxer.occupying.domain.korail.KorailTrain
import com.veluxer.occupying.domain.srt.SrtTrain
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Optional

internal object Fixture {
    const val MOCK_SERVER_HOST = "localhost"
    const val MOCK_SERVER_PORT = 1080
    const val LOGIN_ID = "test-id"
    const val SUCCESS_LOGIN_PW = "test-pw"
    const val FAILURE_LOGIN_PW = "failure-pw"
    const val JSESSIONID = "UXSox5cYRAEd1RjeG5Od0wMPAYiqKtpgTkkfI5la2kbmaITtJ4sYVTU7G4yoGkqa"
    val SEARCH_DEPARTURE_DATETIME: ZonedDateTime = ZonedDateTime.of(2021, 7, 1, 7, 0, 0, 0, ZoneId.systemDefault())
    val SEARCH_DEPARTURE_STATION = Station.SEOUL
    val SEARCH_DESTINATION_STATION = Station.BUSAN
    const val TRAIN_NO = "009"
    val RESERVATION_KORAIL_TRAIN = KorailTrain(
        number = TRAIN_NO,
        trainType = TrainType.KTX.code,
        seatStatus = SeatStatus.AVAILABLE.code.first(),
        fare = "00000000059800",
        departureDate = SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT),
        departureTime = SEARCH_DEPARTURE_DATETIME.toLocalTime().format(TIME_FORMAT),
        departureStation = SEARCH_DEPARTURE_STATION.code,
        arrivalDate = SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT),
        arrivalTime = "093800",
        destinationStation = SEARCH_DESTINATION_STATION.code
    )
    val RESERVATION_SRT_TRAIN = SrtTrain(
        number = TRAIN_NO,
        trainType = TrainType.SRT.code,
        seatStatus = SeatStatus.AVAILABLE.code.first(),
        fare = "00000000052900",
        departureDate = SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT),
        departureTime = SEARCH_DEPARTURE_DATETIME.toLocalTime().format(TIME_FORMAT),
        departureStation = SEARCH_DEPARTURE_STATION.code,
        arrivalDate = SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT),
        arrivalTime = "093800",
        destinationStation = SEARCH_DESTINATION_STATION.code
    )
    val SUCCESS_LOGIN_RESULT = object : LoginResult {
        override fun getToken(): Optional<String> = Optional.of(JSESSIONID)
        override fun isSuccess(): Boolean = true
        override fun getMessage(): String = "정상적으로 조회 되었습니다."
    }
    val FAILURE_LOGIN_RESULT = object : LoginResult {
        override fun getToken(): Optional<String> = Optional.empty()
        override fun isSuccess() = false
        override fun getMessage() = "로그인 정보를 다시 확인하세요."
    }
}
