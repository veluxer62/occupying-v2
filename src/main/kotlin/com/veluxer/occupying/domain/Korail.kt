package com.veluxer.occupying.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.veluxer.occupying.domain.KorailConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.KorailConstraint.SEARCH_PATH
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.net.HttpCookie
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
        return client.post()
            .uri(SEARCH_PATH)
            .body(BodyInserters.fromFormData(filter.getFormData()))
            .retrieve()
            .awaitBody<KorailSearchResponseBody>()
            .trainInformation
            .trains
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
    const val SEARCH_PATH = "/classes/com.korail.mobile.seatMovie.ScheduleView"
}

data class KorailSearchFilter(
    private val departureDateTime: ZonedDateTime,
    private val departureStation: Station,
    private val destinationStation: Station,
) : SearchFilter {
    override fun getFormData(): MultiValueMap<String, String> {
        val data = LinkedMultiValueMap<String, String>()
        data.set("Device", "AD")
        data.set("Version", "190617001")
        data.set("radJobId", "1")
        data.set("selGoTrain", "00")
        data.set("txtPsgFlg_1", "1")
        data.set("txtMenuId", "11")
        data.set("txtGoAbrdDt", departureDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        data.set("txtGoHour", departureDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss")))
        data.set("txtGoStart", departureStation.label)
        data.set("txtGoEnd", destinationStation.label)
        return data
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailSearchResponseBody(
    @JsonProperty("trn_infos")
    val trainInformation: KorailTrainInformation,
)

data class KorailTrainInformation(
    @JsonProperty("trn_info")
    val trains: List<KorailTrain>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KorailTrain(
    @JsonProperty("h_trn_no")
    private val number: String,
    @JsonProperty("h_trn_clsf_cd")
    private val trainType: String,
    @JsonProperty("h_gen_rsv_cd")
    private val seatStatus: String,
    @JsonProperty("h_rcvd_amt")
    private val fare: String,
    @JsonProperty("h_dpt_dt")
    private val departureDate: String,
    @JsonProperty("h_dpt_tm")
    private val departureTime: String,
    @JsonProperty("h_dpt_rs_stn_cd")
    private val departureStation: String,
    @JsonProperty("h_arv_dt")
    private val arrivalDate: String,
    @JsonProperty("h_arv_tm")
    private val arrivalTime: String,
    @JsonProperty("h_arv_rs_stn_cd")
    private val destinationStation: String,
) : Train {
    override fun getNumber(): String = number

    override fun getTrainType(): TrainType = TrainType.values().first { it.code == trainType }

    override fun getSeatStatus(): SeatStatus = SeatStatus.values().first { it.code.contains(seatStatus) }

    override fun getFare(): Int = fare.toInt()

    override fun getDepartureDateTime(): ZonedDateTime = ZonedDateTime
        .of(
            LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
            LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HHmmdd")),
            ZoneId.of("Asia/Seoul")
        )

    override fun getDepartureStation(): Station = Station.values().first { it.code == departureStation }

    override fun getArrivalDateTime(): ZonedDateTime = ZonedDateTime
        .of(
            LocalDate.parse(arrivalDate, DateTimeFormatter.ofPattern("yyyyMMdd")),
            LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("HHmmdd")),
            ZoneId.of("Asia/Seoul")
        )

    override fun getDestinationStation(): Station = Station.values().first { it.code == destinationStation }
}
