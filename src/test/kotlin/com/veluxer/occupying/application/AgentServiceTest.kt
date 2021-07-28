package com.veluxer.occupying.application

import com.veluxer.occupying.Fixture.FAILURE_LOGIN_PW
import com.veluxer.occupying.Fixture.FAILURE_LOGIN_RESULT
import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_RESULT
import com.veluxer.occupying.domain.Agent
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class AgentServiceTest : ExpectSpec({
    val agent = mockk<Agent>()
    val sut = AgentService(agent)

    context("로그인 함수는") {
        coEvery { agent.login(LOGIN_ID, SUCCESS_LOGIN_PW) } returns SUCCESS_LOGIN_RESULT
        coEvery { agent.login(LOGIN_ID, FAILURE_LOGIN_PW) } returns FAILURE_LOGIN_RESULT

        expect("로그인 성공 시 성공응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, SUCCESS_LOGIN_PW)

            actual.getMessage() shouldBe "정상적으로 조회 되었습니다."
            actual.isSuccess() shouldBe true
            actual.getToken().get() shouldBe JSESSIONID
        }

        expect("로그인 실패 시 실패응답을 반환한다") {
            val actual = sut.login(LOGIN_ID, FAILURE_LOGIN_PW)

            actual.getMessage() shouldBe "로그인 정보를 다시 확인하세요."
            actual.isSuccess() shouldBe false
            actual.getToken().isEmpty shouldBe true
        }
    }
})
