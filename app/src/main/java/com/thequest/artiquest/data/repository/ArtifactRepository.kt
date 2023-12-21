package com.thequest.artiquest.data.repository

import com.thequest.artiquest.data.remote.api.response.ArtifactsResponse
import com.thequest.artiquest.data.remote.api.response.DetailArtifactsResponse
import com.thequest.artiquest.data.remote.api.retrofit.ApiService

class ArtifactRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getArtifacts(): ArtifactsResponse {
        return apiService.getArtifacts()
    }

    suspend fun getDetailArtifact(id: String): DetailArtifactsResponse {
        return apiService.getDetailArtifact(id)
    }

    suspend fun searchArtifact(query: String): ArtifactsResponse {
        return apiService.searchArtifacts(query)
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