package com.veluxer.occupying.domain

import java.util.Optional

interface LoginResult {
    fun isSuccess(): Boolean
    fun getMessage(): String
    fun getToken(): Optional<String>
}
