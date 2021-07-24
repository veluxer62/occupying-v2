package com.veluxer.occupying.domain

import com.veluxer.occupying.Fixture.FAILURE_LOGIN_PW
import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.MOCK_SERVER_PORT
import com.veluxer.occupying.Fixture.RESERVATION_KORAIL_TRAIN
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_DATETIME
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_STATION
import com.veluxer.occupying.Fixture.SEARCH_DESTINATION_STATION
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.KorailMockServerListener
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

    context("로그인 함수는") {
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

    context("예약 함수는") {
        expect("로그인 토큰과 열차정보가 주어지면 예약성공 응답을 반환한다") {
            val actual = sut.reserve(JSESSIONID, RESERVATION_KORAIL_TRAIN)

            assertSoftly(actual) {
                isSuccess() shouldBe true
                getMessage() shouldBe "결제하지 않으면 예약이 취소됩니다.\n\n1.승차권 반환수수료는 다음과 같습니다.\n\n- 출발당일 ~1시간전 : 400원\n- 1시간~출발시간전 : 10%\n* 열차출발시각 이후에는 역창구에서 반환하셔야 합니다.\n* 도착역 도착후에는 반환되지 않습니다.\n\n2. 레츠코레일에서 구입한 승차권을 역 창구에서 변경 시 할인이 취소될 수 있습니다.\n\n3. 할인 승차권의 할인율은 별도 공지없이 변경될 수 있습니다.\n\n4. 열차정보, 구입기간, 매수 금액, 주의사항 등을 최종 확인하였습니다.\n1. 스마트폰 승차권은 승차권을 발권한 스마트폰에서만 확인할 수 있습니다.\n\n2. 캡처 사진 등 정당한 승차권을 소지하지 않고 열차를 이용하는 경우, 부가운임을 징수합니다.\n※ 정당승차권에는 코레일 로고가 흐르고 있습니다."
            }
        }
    }
})
