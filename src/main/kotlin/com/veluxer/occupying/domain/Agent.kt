package com.veluxer.occupying.domain

interface Agent {
    suspend fun login(id: String, pw: String): LoginResult
    suspend fun search(filter: SearchFilter): List<Train>
    suspend fun reserve(loginToken: String, train: Train): Result
}
