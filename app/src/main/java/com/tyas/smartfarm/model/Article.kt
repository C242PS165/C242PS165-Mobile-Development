package com.tyas.smartfarm.model

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    val data: List<Article>
)

data class Article(
    val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    @SerializedName("other_name") val otherName: List<String>?,
    val cycle: String,
    val watering: String,
    val sunlight: List<String>,
    @SerializedName("default_image") val defaultImage: DefaultImage?
)

data class DefaultImage(
    val license: Int,
    @SerializedName("license_name") val licenseName: String,
    @SerializedName("license_url") val licenseUrl: String,
    @SerializedName("original_url") val originalUrl: String,
    @SerializedName("regular_url") val regularUrl: String,
    @SerializedName("medium_url") val mediumUrl: String,
    @SerializedName("small_url") val smallUrl: String,
    val thumbnail: String
)
