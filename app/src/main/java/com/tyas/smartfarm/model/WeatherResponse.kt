package com.tyas.smartfarm.model

data class WeatherResponse(
    val success: Boolean,
    val data: List<List<WeatherData>>
)

data class WeatherData(
    val datetime: String,
    val weather_desc: String,
    val temperature: Int,
    val humidity: Int,
    val wind_speed: Double,
    val recommendation: String
)
