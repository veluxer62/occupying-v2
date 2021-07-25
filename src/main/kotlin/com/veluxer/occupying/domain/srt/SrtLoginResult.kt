package com.veluxer.occupying.domain.srt

import com.veluxer.occupying.domain.LoginResult
import org.springframework.http.ResponseEntity
import java.net.HttpCookie
import java.util.Optional

data class SrtLoginResult(private val entity: ResponseEntity<SrtLoginResponseBody>) : LoginResult {
    private val body = entity.body
    private val cookies = entity.headers["Set-Cookie"]?.flatMap { HttpCookie.parse(it) }.orEmpty()

    override fun isSuccess(): Boolean = body?.code.isNullOrEmpty()
    override fun getMessage(): String = body?.message ?: "정상적으로 조회 되었습니다."
    override fun getToken(): Optional<String> = Optional.ofNullable(
        cookies.first { it.name == "JSESSIONID_XEBEC" }.value
    )
}
