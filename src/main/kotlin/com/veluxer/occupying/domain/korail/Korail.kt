package com.veluxer.occupying.domain.korail

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.korail.KorailConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.SEARCH_PATH
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class Korail(private val client: WebClient) : Agent {
    override suspend fun login(id: String, pw: String): LoginResult {
        return client.post()
            .uri(LOGIN_PATH)
            .body(
                BodyInserters
                    .fromFormData("Device", "AD")
                    .with("txtInputFlg", "2")
                    .with("txtMemberNo", id)
                    .with("txtPwd", pw)
            )
            .retrieve()
            .toEntity(KorailLoginResponseBody::class.java)
            .map { KorailLoginResult(it) }
            .awaitSingle()
    }

    override suspend fun search(filter: SearchFilter): List<Train> {
        return client.post()
            .uri(SEARCH_PATH)
            .body(BodyInserters.fromFormData(filter.getFormData()))
            .retrieve()
            .awaitBody<KorailSearchResponseBody>()
            .trainInformation
            .trains
    }

    override suspend fun reserve(loginToken: String, train: Train): Result {
        TODO("Not yet implemented")
    }
}
