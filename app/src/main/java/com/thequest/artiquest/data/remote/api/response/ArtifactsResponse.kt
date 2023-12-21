package com.thequest.artiquest.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ArtifactsResponse(

	@field:SerializedName("data")
	val data: List<ArtifactItem>,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class ArtifactItem(

	@field:SerializedName("image3")
	val image3: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("image1")
	val image1: String? = null,

	@field:SerializedName("image2")
	val image2: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("location")
	val location: String? = null
)
