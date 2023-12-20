package com.thequest.artiquest.data.repository

import com.thequest.artiquest.data.remote.api.response.AgentsResponse
import com.thequest.artiquest.data.remote.api.response.DetailAgentResponse
import com.thequest.artiquest.data.remote.api.retrofit.ApiService

class ArtifactRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getArtifacts(): AgentsResponse {
        return apiService.getAgents()
    }

    suspend fun getDetailArtifact(id: String): DetailAgentResponse {
        return apiService.getDetailAgent(id)
    }

    companion object {
        @Volatile
        private var instance: ArtifactRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ArtifactRepository =
            instance ?: synchronized(this) {
                instance ?: ArtifactRepository(apiService)
            }.also { instance = it }
    }
}