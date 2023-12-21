package com.thequest.artiquest.data.remote.api.retrofit

import com.thequest.artiquest.data.remote.api.response.ArtifactsResponse
import com.thequest.artiquest.data.remote.api.response.DetailArtifactsResponse
import com.thequest.artiquest.data.remote.api.response.PredictArtifactResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("api/allArtifacts")
    suspend fun getArtifacts(): ArtifactsResponse

    @GET("api/artifacts/{id}")
    suspend fun getDetailArtifact(@Path("id") id: String): DetailArtifactsResponse

    @GET("api/artifacts/search/{search}")
    suspend fun searchArtifacts(@Path("search") query: String): ArtifactsResponse

    @Multipart
    @POST("api/artifacts/predict")
    fun scanArtifact(
        @Part file: MultipartBody.Part
    ): Call<PredictArtifactResponse>
}