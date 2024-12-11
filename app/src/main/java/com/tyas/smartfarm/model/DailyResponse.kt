package com.tyas.smartfarm.model

import com.google.gson.annotations.SerializedName

data class DailyResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("location")
	val location: String? = null
)

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("summary")
	val summary: String? = null,

	val iconResId: Int

)
