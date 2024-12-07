package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentWeatherBinding
import com.tyas.smartfarm.view.adapter.DailyForecastAdapter
import com.tyas.smartfarm.view.adapter.DailyWeather
import com.tyas.smartfarm.view.adapter.HourlyForecastAdapter
import com.tyas.smartfarm.view.adapter.HourlyWeather
import com.tyas.smartfarm.view.pages.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data statis untuk Hourly Forecast
        val hourlyData = listOf(
            HourlyWeather("12:00", "27°", R.drawable.ic_cloudy),
            HourlyWeather("13:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("14:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("15:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("16:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("17:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("18:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("19:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("20:00", "26°", R.drawable.ic_cloudy),
            HourlyWeather("21:00", "26°", R.drawable.ic_cloudy)
        )

        // Adapter untuk Hourly Forecast
        val hourlyAdapter = HourlyForecastAdapter(hourlyData)
        binding.hourlyForecastRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecycler.adapter = hourlyAdapter



        // Observasi LiveData dari ViewModel untuk Daily Forecast
        weatherViewModel.weatherData.observe(viewLifecycleOwner) { data ->
            val dailyAdapter = DailyForecastAdapter(data.map {
                DailyWeather(
                    it.datetime,
                    it.weather_desc,
                    "${it.temperature}°",
                    getWeatherIcon(it.weather_desc) // Gunakan fungsi untuk mendapatkan ikon
                )
            })
            binding.dailyForecastRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.dailyForecastRecycler.adapter = dailyAdapter
        }


        // Observasi status loading
        weatherViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Menangani pesan error
        weatherViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        // Memanggil data cuaca
        weatherViewModel.fetchWeatherData()
    }

    // Fungsi untuk memetakan deskripsi cuaca ke ikon
    private fun getWeatherIcon(weatherDesc: String?): Int {
        return when (weatherDesc?.lowercase()) {
            "berawan" -> R.drawable.ic_cloudy
            "petir" -> R.drawable.ic_thunderstorm
            "hujan ringan" -> R.drawable.ic_light_rain
            "hujan petir" -> R.drawable.ic_rain_thunderstorm
            "cerah berawan" -> R.drawable.ic_partly_cloudy
            "cerah" -> R.drawable.ic_sunny
            "udara kabur" -> R.drawable.ic_hazy
            else -> R.drawable.placeholder_image // Default icon jika deskripsi tidak cocok
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
