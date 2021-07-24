package com.veluxer.occupying.domain.korail

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.PassengerType
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.SeatType
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.korail.KorailConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.korail.KorailConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.RESERVATION_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.SEARCH_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.TIME_FORMAT
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
        val departureDateTime = train.getDepartureDateTime()
        return client.post()
            .uri(RESERVATION_PATH)
            .header("cookie", "JSESSIONID=$loginToken")
            .body(
                BodyInserters
                    .fromFormData("Device", "AD")
                    .with("txtSeatAttCd1", SeatType.NORMAL.code)
                    .with("txtSeatAttCd2", "000")
                    .with("txtSeatAttCd3", "000")
                    .with("txtSeatAttCd4", "015")
                    .with("txtStndFlg", "N")
                    .with("txtJrnyCnt", "1")
                    .with("txtJrnySqno1", "001")
                    .with("txtJrnyTpCd1", "11")
                    .with("txtTotPsgCnt", "1")
                    .with("txtDptRsStnCd1", train.getDepartureStation().code)
                    .with("txtDptDt1", departureDateTime.toLocalDate().format(DATE_FORMAT))
                    .with("txtDptTm1", departureDateTime.toLocalTime().format(TIME_FORMAT))
                    .with("txtArvRsStnCd1", train.getDestinationStation().code)
                    .with("txtTrnNo1", train.getNumber())
                    .with("txtTrnClsfCd1", train.getTrainType().code)
                    .with("txtTrnGpCd1", "100")
                    .with("txtPsgTpCd1", PassengerType.ADULT_YOUTH.code)
                    .with("txtDiscKndCd1", "000")
                    .with("txtCompaCnt1", "1")
            )
            .retrieve()
            .awaitBody<KorailReservationResult>()
    }
}
