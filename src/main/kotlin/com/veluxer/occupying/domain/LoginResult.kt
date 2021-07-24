package com.veluxer.occupying.domain

import java.util.Optional

interface LoginResult : Result {
    fun getToken(): Optional<String>
}
