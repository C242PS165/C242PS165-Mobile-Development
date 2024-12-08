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



        // Observasi data dari ViewModel
        weatherViewModel.location.observe(viewLifecycleOwner) { location ->
            binding.locationText.text = location
        }

        weatherViewModel.currentTemperature.observe(viewLifecycleOwner) { temp ->
            binding.temperatureText.text = temp
        }

        weatherViewModel.weatherCondition.observe(viewLifecycleOwner) { condition ->
            binding.weatherConditionText.text = condition
        }

        weatherViewModel.airQuality.observe(viewLifecycleOwner) { airQuality ->
            val airQualityIndex = getAirQualityIndexLabel(airQuality) // Mendapatkan label AQI
            binding.airQualityText.text = "Kualitas Udara: $airQuality ($airQualityIndex)"
        }


        weatherViewModel.weatherIcon.observe(viewLifecycleOwner) { iconResId ->
            binding.weatherIcon.setImageResource(iconResId)
        }

        // Observasi data harian
        weatherViewModel.weatherData.observe(viewLifecycleOwner) { data ->
            val dailyAdapter = DailyForecastAdapter(data.map {
                DailyWeather(
                    it.datetime,
                    it.weather_desc,
                    "${it.temperature}°",
                    weatherViewModel.getWeatherIcon(it.weather_desc) // Panggil fungsi dari ViewModel
                )
            })
            binding.dailyForecastRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.dailyForecastRecycler.adapter = dailyAdapter
        }

        // Observasi pesan cuaca
        weatherViewModel.weatherMessage.observe(viewLifecycleOwner) { message ->
            binding.weatherMessageText.text = message
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

        // Panggil data cuaca
        weatherViewModel.fetchWeatherData()
    }

    private fun getAirQualityIndexLabel(airQuality: Int): String {
        return when (airQuality) {
            in 0..50 -> getString(R.string.air_quality_good)
            in 51..100 -> getString(R.string.air_quality_moderate)
            in 101..150 -> getString(R.string.air_quality_unhealthy_sensitive)
            in 151..200 -> getString(R.string.air_quality_unhealthy)
            in 201..300 -> getString(R.string.air_quality_very_unhealthy)
            else -> getString(R.string.air_quality_hazardous)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
