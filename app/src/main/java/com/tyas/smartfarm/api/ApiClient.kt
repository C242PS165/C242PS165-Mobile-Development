package com.tyas.smartfarm.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Base URL untuk Weather API
    private const val WEATHER_BASE_URL = "https://api-prediction-697742155891.asia-southeast2.run.app/api/"

    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    // Base URL untuk Article API
    private const val ARTICLE_BASE_URL = "https://perenual.com/api/"

    val articleApiService: ArticleApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ARTICLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArticleApiService::class.java)
    }

    // Base URL untuk Google Gemini API
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    val geminiApiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}
