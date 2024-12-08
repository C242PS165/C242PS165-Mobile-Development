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

    private fun generateWeatherMessage(weatherDesc: String?): String {
        val context = getApplication<Application>()
        return when (weatherDesc?.lowercase()) {
            "berawan" -> context.getString(R.string.weather_message_cloudy)
            "petir" -> context.getString(R.string.weather_message_thunderstorm)
            "hujan ringan" -> context.getString(R.string.weather_message_light_rain)
            "hujan petir" -> context.getString(R.string.weather_message_rain_thunderstorm)
            "cerah berawan" -> context.getString(R.string.weather_message_partly_cloudy)
            "cerah" -> context.getString(R.string.weather_message_sunny)
            "udara kabur" -> context.getString(R.string.weather_message_hazy)
            else -> context.getString(R.string.weather_message_unknown)
        }
    }




    // Fungsi untuk memetakan deskripsi cuaca ke ikon
    fun getWeatherIcon(weatherDesc: String?): Int {
        return when (weatherDesc?.lowercase()) {
            "berawan" -> R.drawable.img
            "petir" -> R.drawable.ic_thunderstorm
            "hujan ringan" -> R.drawable.ic_light_rain
            "hujan petir" -> R.drawable.ic_rain_thunderstorm
            "cerah berawan" -> R.drawable.ic_partly_cloudy
            "cerah" -> R.drawable.ic_sunny
            "udara kabur" -> R.drawable.ic_hazy
            else -> R.drawable.placeholder_image // Default icon jika deskripsi tidak cocok
        }
    }


}
