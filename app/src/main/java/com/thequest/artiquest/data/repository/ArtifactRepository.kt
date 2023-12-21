package com.thequest.artiquest.data.repository

import com.thequest.artiquest.data.remote.api.response.AgentsResponse
import com.thequest.artiquest.data.remote.api.response.ArtifactsResponse
import com.thequest.artiquest.data.remote.api.response.DetailAgentResponse
import com.thequest.artiquest.data.remote.api.retrofit.ApiService

class ArtifactRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getArtifacts(): ArtifactsResponse {
        return apiService.getArtifacts()
    }

    suspend fun getDetailArtifact(id: String): DetailAgentResponse  { // Response ntar diganti
        return apiService.getDetailArtifact(id)
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