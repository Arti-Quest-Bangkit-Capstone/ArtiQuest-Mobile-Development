package com.thequest.artiquest.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class PredictArtifactResponse(

	@field:SerializedName("predicted_class")
	val predictedClass: String? = null,

	@field:SerializedName("id_class")
	val idClass: Int? = null
)
