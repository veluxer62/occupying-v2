package com.veluxer.occupying.domain

interface Result {
    fun isSuccess(): Boolean
    fun getMessage(): String
}
