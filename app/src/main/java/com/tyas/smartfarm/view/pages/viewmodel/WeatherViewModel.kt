package com.tyas.smartfarm.view.pages.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.tyas.smartfarm.R
import com.tyas.smartfarm.api.ApiClient
import com.tyas.smartfarm.model.WeatherData
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData yang sudah ada
    val weatherData = MutableLiveData<List<WeatherData>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    // LiveData baru
    val location = MutableLiveData<String>() // Lokasi
    val currentTemperature = MutableLiveData<String>() // Suhu
    val weatherCondition = MutableLiveData<String>() // Deskripsi cuaca
    val airQuality = MutableLiveData<Int>() // Kualitas udara
    val weatherIcon = MutableLiveData<Int>() // Ikon cuaca
    val weatherMessage = MutableLiveData<String>()

    fun fetchWeatherData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = ApiClient.weatherApiService.getWeatherData()
                if (response.isSuccessful && response.body()?.success == true) {
                    val dataItem = response.body()?.data?.get(0)?.get(0) // Ambil data hari pertama
                    weatherData.value = response.body()?.data?.get(0) // Semua data cuaca

                    // Ambil detail untuk bagian atas
                    dataItem?.let {
                        location.value = "Cilegon" // Lokasi statis atau bisa diubah
                        currentTemperature.value = "${it.temperature}°"
                        weatherCondition.value = it.weather_desc ?: "Cuaca tidak diketahui"
                        airQuality.value = it.humidity

                        // Tetapkan ikon berdasarkan deskripsi cuaca
                        weatherIcon.value = getWeatherIcon(it.weather_desc)

                        // Tetapkan pesan berdasarkan kondisi cuaca
                        weatherMessage.value = generateWeatherMessage(it.weather_desc)
                    }
                } else {
                    errorMessage.value = "Gagal memuat data cuaca"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    object WeatherDescriptions {
        const val CLOUDY = "berawan"
        const val THUNDERSTORM = "petir"
        const val LIGHT_RAIN = "hujan ringan"
        const val RAIN_THUNDERSTORM = "hujan petir"
        const val PARTLY_CLOUDY = "cerah berawan"
        const val SUNNY = "cerah"
        const val HAZY = "udara kabur"
    }

    private fun generateWeatherMessage(weatherDesc: String?): String {
        val context = getApplication<Application>()
        return when (weatherDesc?.lowercase()) {
            WeatherDescriptions.CLOUDY -> context.getString(R.string.weather_message_cloudy)
            WeatherDescriptions.THUNDERSTORM -> context.getString(R.string.weather_message_thunderstorm)
            WeatherDescriptions.LIGHT_RAIN -> context.getString(R.string.weather_message_light_rain)
            WeatherDescriptions.RAIN_THUNDERSTORM -> context.getString(R.string.weather_message_rain_thunderstorm)
            WeatherDescriptions.PARTLY_CLOUDY -> context.getString(R.string.weather_message_partly_cloudy)
            WeatherDescriptions.SUNNY -> context.getString(R.string.weather_message_sunny)
            WeatherDescriptions.HAZY -> context.getString(R.string.weather_message_hazy)
            else -> context.getString(R.string.weather_message_unknown)
        }
    }



    // Fungsi untuk memetakan deskripsi cuaca ke ikon
    fun getWeatherIcon(weatherDesc: String?): Int {
        return when (weatherDesc?.lowercase()) {
            WeatherDescriptions.CLOUDY -> R.drawable.img
            WeatherDescriptions.THUNDERSTORM -> R.drawable.ic_thunderstorm
            WeatherDescriptions.LIGHT_RAIN -> R.drawable.ic_light_rain
            WeatherDescriptions.RAIN_THUNDERSTORM -> R.drawable.ic_rain_thunderstorm
            WeatherDescriptions.PARTLY_CLOUDY -> R.drawable.ic_partly_cloudy
            WeatherDescriptions.SUNNY -> R.drawable.ic_sunny
            WeatherDescriptions.HAZY -> R.drawable.ic_hazy
            else -> R.drawable.placeholder_image
        }
    }



}
