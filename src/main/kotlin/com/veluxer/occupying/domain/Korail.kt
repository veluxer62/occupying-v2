package com.veluxer.occupying.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.KorailConstraint.LOGIN_PATH
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.net.HttpCookie
import java.util.Optional

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
        TODO("Not yet implemented")
    }

    override suspend fun reserve(loginToken: String, train: Train) {
        TODO("Not yet implemented")
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailLoginResponseBody(
    @JsonProperty("h_msg_txt")
    val message: String,
    @JsonProperty("h_msg_cd")
    val code: String,
)

data class KorailLoginResult(private val entity: ResponseEntity<KorailLoginResponseBody>) : LoginResult {
    private val body = entity.body
    private val cookies = entity.headers["Set-Cookie"]?.joinToString(";")?.let { HttpCookie.parse(it) }
    private val loginSuccessCode = "IRZ000001"

    override fun isSuccess(): Boolean = body?.code == loginSuccessCode
    override fun getMessage(): String = body?.message.orEmpty()
    override fun getToken(): Optional<String> = Optional.ofNullable(
        cookies?.first { cookie -> cookie.name == "JSESSIONID" }?.value
    )
}

object KorailConstraint {
    const val LOGIN_PATH = "/classes/com.korail.mobile.login.Login"
}
