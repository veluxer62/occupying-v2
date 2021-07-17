package com.veluxer.occupying.domain.korail

import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Station
import com.veluxer.occupying.domain.korail.KorailConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.korail.KorailConstraint.TIME_FORMAT
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.ZonedDateTime

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
        data.set("txtGoAbrdDt", departureDateTime.toLocalDate().format(DATE_FORMAT))
        data.set("txtGoHour", departureDateTime.toLocalTime().format(TIME_FORMAT))
        data.set("txtGoStart", departureStation.label)
        data.set("txtGoEnd", destinationStation.label)
        return data
    }
}
