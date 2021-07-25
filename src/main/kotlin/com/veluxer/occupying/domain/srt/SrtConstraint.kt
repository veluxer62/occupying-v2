package com.veluxer.occupying.domain.srt

import java.time.format.DateTimeFormatter

object SrtConstraint {
    const val LOGIN_PATH = "/apb/selectListApb01080_n.do"
    const val SEARCH_PATH = "/ara/selectListAra10007_n.do"
    val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss")
}
