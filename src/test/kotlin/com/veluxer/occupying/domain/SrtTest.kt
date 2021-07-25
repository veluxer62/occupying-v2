package com.veluxer.occupying.domain

import com.veluxer.occupying.Fixture
import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.MOCK_SERVER_PORT
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.SrtMockServerListener
import com.veluxer.occupying.application.AppConfig
import com.veluxer.occupying.domain.srt.Srt
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.mockserver.MockServerListener
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient

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
})
