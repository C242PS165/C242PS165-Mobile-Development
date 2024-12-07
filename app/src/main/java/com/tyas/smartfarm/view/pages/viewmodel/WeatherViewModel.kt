import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.R
import com.tyas.smartfarm.api.ApiClient
import com.tyas.smartfarm.model.WeatherData
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
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
