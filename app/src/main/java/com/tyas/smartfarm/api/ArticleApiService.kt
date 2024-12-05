package com.tyas.smartfarm.api

import com.tyas.smartfarm.model.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiService {

    @GET("species-list")
    fun getPlantArticles(@Query("key") apiKey: String): Call<ArticleResponse>
}
