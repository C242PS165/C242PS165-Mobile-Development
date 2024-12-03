package com.tyas.smartfarm.api

import com.tyas.smartfarm.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApiService {
    @GET("forecast/35.19.05.2002")
    suspend fun getWeatherData(): Response<WeatherResponse>
}
