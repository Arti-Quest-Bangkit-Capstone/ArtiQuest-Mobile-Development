package com.thequest.artiquest.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class DetailArtifactsResponse(

	@field:SerializedName("data")
	val data: Artifact? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Artifact(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
