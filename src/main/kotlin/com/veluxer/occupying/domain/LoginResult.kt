package com.veluxer.occupying.domain

interface LoginResult {
    fun isSuccess(): Boolean
    fun getMessage(): String
    fun getToken(): String
}
