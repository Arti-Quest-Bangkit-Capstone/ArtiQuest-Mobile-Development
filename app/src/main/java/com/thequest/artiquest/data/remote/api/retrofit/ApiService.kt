package com.thequest.artiquest.data.remote.api.retrofit

import com.thequest.artiquest.data.remote.api.response.AgentsResponse
import com.thequest.artiquest.data.remote.api.response.DetailAgentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("agents")
    suspend fun getAgents(): AgentsResponse

    @GET("agents/{uuid}")
    suspend fun getDetailAgent(@Path("uuid") uuid: String): DetailAgentResponse
}