package com.tyas.smartfarm.view.pages.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.model.WeatherData
import com.tyas.smartfarm.api.ApiClient
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weatherData = MutableLiveData<List<WeatherData>>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun fetchWeatherData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = ApiClient.weatherApiService.getWeatherData()
                if (response.isSuccessful && response.body()?.success == true) {
                    weatherData.value = response.body()?.data?.get(0) // Data untuk hari pertama
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
}
