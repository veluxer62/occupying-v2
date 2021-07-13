package com.veluxer.occupying.domain

import com.veluxer.occupying.KorailMockServerListener
import com.veluxer.occupying.TestConstraint.FAILURE_LOGIN_PW
import com.veluxer.occupying.TestConstraint.JSESSIONID
import com.veluxer.occupying.TestConstraint.LOGIN_ID
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_PORT
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
})
