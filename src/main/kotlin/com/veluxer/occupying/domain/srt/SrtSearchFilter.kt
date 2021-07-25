package com.veluxer.occupying.domain.srt

import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Station
import com.veluxer.occupying.domain.korail.KorailConstraint
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.ZonedDateTime

data class SrtSearchFilter(
    private val departureDateTime: ZonedDateTime,
    private val departureStation: Station,
    private val destinationStation: Station,
) : SearchFilter {
    override fun getFormData(): MultiValueMap<String, String> {
        val data = LinkedMultiValueMap<String, String>()
        data.set("chtnDvCd", "1")
        data.set("seatAttCd", "015")
        data.set("psgNum", "1")
        data.set("trnGpCd", "109")
        data.set("stlbTrnClsfCd", "05")
        data.set("dptDt", departureDateTime.toLocalDate().format(KorailConstraint.DATE_FORMAT))
        data.set("dptTm", departureDateTime.toLocalTime().format(KorailConstraint.TIME_FORMAT))
        data.set("dptRsStnCd", departureStation.code)
        data.set("arvRsStnCd", destinationStation.code)
        return data
    }
}
