package com.veluxer.occupying.domain

import com.veluxer.occupying.KorailMockServerListener
import com.veluxer.occupying.TestConstraint.JSESSIONID
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_PORT
import com.veluxer.occupying.TestConstraint.SUCCESS_LOGIN_ID
import com.veluxer.occupying.TestConstraint.SUCCESS_LOGIN_PW
import com.veluxer.occupying.application.AppConfig
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.mockserver.MockServerListener
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient

@ActiveProfiles("test")
@SpringBootTest(classes = [AppConfig::class])
class KorailTest(korailClient: WebClient) : ExpectSpec({

    listeners(MockServerListener(MOCK_SERVER_PORT), KorailMockServerListener())

    val sut = Korail(korailClient)

    context("Korail 로그인 함수는") {
        expect("로그인 성공 시 성공응답을 반환한다") {
            val actual = sut.login(SUCCESS_LOGIN_ID, SUCCESS_LOGIN_PW)

            actual.getMessage() shouldBe "정상적으로 조회 되었습니다."
            actual.isSuccess() shouldBe true
            actual.getToken() shouldBe JSESSIONID
        }
    }
})
