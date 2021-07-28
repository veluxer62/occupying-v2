package com.veluxer.occupying.domain

import com.veluxer.occupying.Fixture
import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.MOCK_SERVER_PORT
import com.veluxer.occupying.Fixture.RESERVATION_SRT_TRAIN
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_DATETIME
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_STATION
import com.veluxer.occupying.Fixture.SEARCH_DESTINATION_STATION
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.SrtMockServerListener
import com.veluxer.occupying.application.AppConfig
import com.veluxer.occupying.domain.srt.Srt
import com.veluxer.occupying.domain.srt.SrtSearchFilter
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.mockserver.MockServerListener
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(classes = [AppConfig::class])
internal class SrtTest(srtClient: WebClient) : ExpectSpec({
    listeners(MockServerListener(MOCK_SERVER_PORT), SrtMockServerListener())

    val sut = Srt(srtClient)

    context("로그인 함수는") {
        expect("로그인 성공 시 성공응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, SUCCESS_LOGIN_PW)

            actual.getMessage() shouldBe "정상적으로 조회 되었습니다."
            actual.isSuccess() shouldBe true
            actual.getToken().get() shouldBe JSESSIONID
        }

        expect("로그인 실패 시 실패응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, Fixture.FAILURE_LOGIN_PW)

            actual.getMessage() shouldBe "비밀번호 오류입니다."
            actual.isSuccess() shouldBe false
            actual.getToken().isEmpty shouldBe true
        }
    }

    expect("열차 조회 함수는 검색 필터를 입력하면 열차목록을 반환한다") {
        val filter = SrtSearchFilter(
            SEARCH_DEPARTURE_DATETIME,
            SEARCH_DEPARTURE_STATION,
            SEARCH_DESTINATION_STATION
        )

        val actual = sut.search(filter)

        assertSoftly(actual[0]) {
            getNumber() shouldBe "301"
            getTrainType() shouldBe TrainType.SRT
            getSeatStatus() shouldBe SeatStatus.AVAILABLE
            getFare() shouldBe 52900
            getDepartureDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 5, 30, 0, 0, ZoneId.systemDefault())
            getDepartureStation() shouldBe Station.SUSEO
            getArrivalDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 8, 5, 0, 0, ZoneId.systemDefault())
            getDestinationStation() shouldBe Station.BUSAN
        }

        assertSoftly(actual[1]) {
            getNumber() shouldBe "349"
            getTrainType() shouldBe TrainType.SRT
            getSeatStatus() shouldBe SeatStatus.SOLD_OUT
            getFare() shouldBe 52900
            getDepartureDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 16, 21, 0, 0, ZoneId.systemDefault())
            getDepartureStation() shouldBe Station.SUSEO
            getArrivalDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 18, 46, 0, 0, ZoneId.systemDefault())
            getDestinationStation() shouldBe Station.BUSAN
        }
    }

    context("예약 함수는") {
        expect("로그인 토큰과 열차정보가 주어지면 예약성공 응답을 반환한다") {
            val actual = sut.reserve(JSESSIONID, RESERVATION_SRT_TRAIN)

            assertSoftly(actual) {
                isSuccess() shouldBe true
                getMessage() shouldBe "결제하지 않으면 예약이 취소됩니다."
            }
        }

        expect("로그인 토큰이 유효하지 않는 경우 예약실패 응답을 반환한다") {
            val actual = sut.reserve(UUID.randomUUID().toString(), RESERVATION_SRT_TRAIN)

            assertSoftly(actual) {
                isSuccess() shouldBe false
                getMessage() shouldBe "로그인 후 사용하십시요."
            }
        }
    }
})
