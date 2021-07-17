package com.veluxer.occupying.domain.korail

import com.veluxer.occupying.domain.LoginResult
import org.springframework.http.ResponseEntity
import java.net.HttpCookie
import java.util.Optional

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
