package com.veluxer.occupying.domain.srt

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train
import org.springframework.web.reactive.function.client.WebClient

class Srt(private val client: WebClient) : Agent {
    override suspend fun login(id: String, pw: String): LoginResult {
        TODO("Not yet implemented")
    }

    override suspend fun search(filter: SearchFilter): List<Train> {
        TODO("Not yet implemented")
    }

    override suspend fun reserve(loginToken: String, train: Train): Result {
        TODO("Not yet implemented")
    }
}
