package com.thequest.artiquest.data.remote.api.retrofit

import com.thequest.artiquest.data.remote.api.response.AgentsResponse
import com.thequest.artiquest.data.remote.api.response.ArtifactsResponse
import com.thequest.artiquest.data.remote.api.response.DetailAgentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/allArtifacts")
    suspend fun getArtifacts(): ArtifactsResponse

    @GET("api/artifacts/{id}")
    suspend fun getDetailArtifact(@Path("id") id: String): DetailAgentResponse // Ntar diganti pake detail kita
}