package com.veluxer.occupying.domain

import com.veluxer.occupying.KorailMockServerListener
import com.veluxer.occupying.TestConstraint.FAILURE_LOGIN_PW
import com.veluxer.occupying.TestConstraint.JSESSIONID
import com.veluxer.occupying.TestConstraint.LOGIN_ID
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_PORT
import com.veluxer.occupying.TestConstraint.SEARCH_DEPARTURE_DATETIME
import com.veluxer.occupying.TestConstraint.SEARCH_DEPARTURE_STATION
import com.veluxer.occupying.TestConstraint.SEARCH_DESTINATION_STATION
import com.veluxer.occupying.TestConstraint.SUCCESS_LOGIN_PW
import com.veluxer.occupying.application.AppConfig
import com.veluxer.occupying.domain.korail.Korail
import com.veluxer.occupying.domain.korail.KorailSearchFilter
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.mockserver.MockServerListener
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient
import java.time.ZoneId
import java.time.ZonedDateTime

@ActiveProfiles("test")
@SpringBootTest(classes = [AppConfig::class])
class KorailTest(korailClient: WebClient) : ExpectSpec({

    listeners(MockServerListener(MOCK_SERVER_PORT), KorailMockServerListener())

    val sut = Korail(korailClient)

    context("Korail 로그인 함수는") {
        expect("로그인 성공 시 성공응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, SUCCESS_LOGIN_PW)

            actual.getMessage() shouldBe "정상적으로 조회 되었습니다."
            actual.isSuccess() shouldBe true
            actual.getToken().get() shouldBe JSESSIONID
        }

        expect("로그인 실패 시 실패응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, FAILURE_LOGIN_PW)

            actual.getMessage() shouldBe "로그인 정보를 다시 확인하세요.\n코레일 멤버십에 등록되지 않은 정보이거나 회원번호(이메일/휴대전화) 또는 비밀번호를 잘못 입력하셨습니다.\n비밀번호는 연속 5회 틀리면 이용이 불가하니 주의 바랍니다."
            actual.isSuccess() shouldBe false
            actual.getToken().isEmpty shouldBe true
        }
    }

    expect("열차 조회 함수는 검색 필터를 입력하면 열차목록을 반환한다") {
        val filter = KorailSearchFilter(
            SEARCH_DEPARTURE_DATETIME,
            SEARCH_DEPARTURE_STATION,
            SEARCH_DESTINATION_STATION
        )

        val actual = sut.search(filter)

        assertSoftly(actual[0]) {
            getNumber() shouldBe "009"
            getTrainType() shouldBe TrainType.KTX
            getSeatStatus() shouldBe SeatStatus.AVAILABLE
            getFare() shouldBe 59800
            getDepartureDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 7, 0, 0, 0, ZoneId.systemDefault())
            getDepartureStation() shouldBe Station.SEOUL
            getArrivalDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 9, 38, 0, 0, ZoneId.systemDefault())
            getDestinationStation() shouldBe Station.BUSAN
        }

        assertSoftly(actual[1]) {
            getNumber() shouldBe "011"
            getTrainType() shouldBe TrainType.KTX
            getSeatStatus() shouldBe SeatStatus.AVAILABLE
            getFare() shouldBe 53900
            getDepartureDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 7, 30, 0, 0, ZoneId.systemDefault())
            getDepartureStation() shouldBe Station.SEOUL
            getArrivalDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 10, 3, 0, 0, ZoneId.systemDefault())
            getDestinationStation() shouldBe Station.BUSAN
        }

        assertSoftly(actual[2]) {
            getNumber() shouldBe "025"
            getTrainType() shouldBe TrainType.KTX
            getSeatStatus() shouldBe SeatStatus.SOLD_OUT
            getFare() shouldBe 59800
            getDepartureDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 11, 0, 0, 0, ZoneId.systemDefault())
            getDepartureStation() shouldBe Station.SEOUL
            getArrivalDateTime() shouldBe ZonedDateTime.of(2021, 7, 1, 13, 41, 0, 0, ZoneId.systemDefault())
            getDestinationStation() shouldBe Station.BUSAN
        }
    }
})
