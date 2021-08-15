package com.veluxer.occupying.application

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train
import com.veluxer.occupying.domain.TrainType

class TrainTicketBox(
    private val korail: Agent,
    private val srt: Agent,
) {
    suspend fun login(type: TrainType, id: String, pw: String): LoginResult {
        return korail.login(id, pw)
    }

    suspend fun search(filter: SearchFilter): List<Train> {
        TODO("Not yet implemented")
    }

    suspend fun reserve(loginToken: String, train: Train): Result {
        TODO("Not yet implemented")
    }
}
