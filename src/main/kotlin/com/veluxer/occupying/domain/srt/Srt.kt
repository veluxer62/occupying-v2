package com.veluxer.occupying.domain.srt

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.srt.SrtConstraint.LOGIN_PATH
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

class Srt(private val client: WebClient) : Agent {
    override suspend fun login(id: String, pw: String): LoginResult {
        return client.post()
            .uri(LOGIN_PATH)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6"
            )
            .header("Accept", "application/json")
            .body(
                BodyInserters
                    .fromFormData("auto", "Y")
                    .with("check", "Y")
                    .with("srchDvCd", "1")
                    .with("srchDvNm", id)
                    .with("hmpgPwdCphd", pw)
            )
            .retrieve()
            .toEntity(SrtLoginResponseBody::class.java)
            .map { SrtLoginResult(it) }
            .awaitSingle()
    }

    override suspend fun search(filter: SearchFilter): List<Train> {
        TODO("Not yet implemented")
    }

    override suspend fun reserve(loginToken: String, train: Train): Result {
        TODO("Not yet implemented")
    }
}
