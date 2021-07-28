package com.veluxer.occupying.domain.srt

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.srt.SrtConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.srt.SrtConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.srt.SrtConstraint.RESERVATION_PATH
import com.veluxer.occupying.domain.srt.SrtConstraint.SEARCH_PATH
import com.veluxer.occupying.domain.srt.SrtConstraint.SESSION_COOKIE_NAME
import com.veluxer.occupying.domain.srt.SrtConstraint.TIME_FORMAT
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class Srt(private val client: WebClient) : Agent {
    override suspend fun login(id: String, pw: String): LoginResult {
        return client.post()
            .uri(LOGIN_PATH)
            .headers(setDefaultHeader())
            .body(generateLoginRequestBody(id, pw))
            .retrieve()
            .toEntity(SrtLoginResponseBody::class.java)
            .map { SrtLoginResult(it) }
            .awaitSingle()
    }

    override suspend fun search(filter: SearchFilter): List<Train> {
        return client.post()
            .uri(SEARCH_PATH)
            .headers(setDefaultHeader())
            .body(BodyInserters.fromFormData(filter.getFormData()))
            .retrieve()
            .awaitBody<SrtSearchResponseBody>()
            .trains
    }

    override suspend fun reserve(loginToken: String, train: Train): Result {
        return client.post()
            .uri(RESERVATION_PATH)
            .headers(setDefaultHeader())
            .cookie(SESSION_COOKIE_NAME, loginToken)
            .body(generateReservationRequestBody(train))
            .retrieve()
            .awaitBody<SrtReservationResult>()
    }

    private fun generateReservationRequestBody(train: Train) = BodyInserters
        .fromFormData("reserveType", "11")
        .with("jobId", "1101")
        .with("jrnyCnt", "1")
        .with("jrnyTpCd", "11")
        .with("jrnySqno1", "001")
        .with("stndFlg", "N")
        .with("trnGpCd1", "300")
        .with("totPrnb", "1")
        .with("psgGridcnt", "1")
        .with("psgTpCd1", "1")
        .with("psgInfoPerPrnb1", "1")
        .with("locSeatAttCd1", "000")
        .with("rqSeatAttCd1", "015")
        .with("dirSeatAttCd1", "009")
        .with("smkSeatAttCd1", "000")
        .with("etcSeatAttCd1", "000")
        .with("psrmClCd1", "1")
        .with("stlbTrnClsfCd1", train.getTrainType().code)
        .with("trnNo1", train.getNumber().padStart(5, '0'))
        .with("dptDt1", train.getDepartureDateTime().toLocalDate().format(DATE_FORMAT))
        .with("dptTm1", train.getDepartureDateTime().toLocalTime().format(TIME_FORMAT))
        .with("runDt1", train.getDepartureDateTime().toLocalDate().format(DATE_FORMAT))
        .with("dptRsStnCd1", train.getDepartureStation().code)
        .with("arvRsStnCd1", train.getDestinationStation().code)

    private fun generateLoginRequestBody(
        id: String,
        pw: String,
    ) = BodyInserters
        .fromFormData("auto", "Y")
        .with("check", "Y")
        .with("srchDvCd", "1")
        .with("srchDvNm", id)
        .with("hmpgPwdCphd", pw)

    private fun setDefaultHeader(): (t: HttpHeaders) -> Unit = {
        it.accept = listOf(MediaType.APPLICATION_JSON)
        it.set(
            HttpHeaders.USER_AGENT,
            "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6"
        )
    }
}
