package com.thequest.artiquest.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class AgentsResponse(

	@field:SerializedName("data")
	val data: List<AgentItem>,

	@field:SerializedName("status")
	val status: Int? = null
)


data class AgentItem(

	@field:SerializedName("killfeedPortrait")
	val killfeedPortrait: String? = null,

	@field:SerializedName("displayName")
	val displayName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("displayIconSmall")
	val displayIconSmall: String? = null,

	@field:SerializedName("fullPortrait")
	val fullPortrait: String? = null,

	@field:SerializedName("fullPortraitV2")
	val fullPortraitV2: String? = null,

	@field:SerializedName("displayIcon")
	val displayIcon: String? = null,

	@field:SerializedName("bustPortrait")
	val bustPortrait: String? = null,

	@field:SerializedName("background")
	val background: String? = null,
)


