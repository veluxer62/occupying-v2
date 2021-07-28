package com.veluxer.occupying.application

import com.veluxer.occupying.domain.Agent
import com.veluxer.occupying.domain.LoginResult
import com.veluxer.occupying.domain.Result
import com.veluxer.occupying.domain.SearchFilter
import com.veluxer.occupying.domain.Train

class AgentService(private val agent: Agent) : Agent {
    override suspend fun login(id: String, pw: String): LoginResult {
        return agent.login(id, pw)
    }

    override suspend fun search(filter: SearchFilter): List<Train> {
        TODO("Not yet implemented")
    }

    override suspend fun reserve(loginToken: String, train: Train): Result {
        TODO("Not yet implemented")
    }
}
