package com.tyas.smartfarm.api

import com.tyas.smartfarm.model.DailyResponse
import com.tyas.smartfarm.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApiService {
    @GET("forecast/35.73.04.1002")
    suspend fun getWeatherData(): Response<WeatherResponse>

    @GET("simplified/35.73.04.1002")
    suspend fun getSimplifiedWeatherData(): Response<DailyResponse>
}
