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
import com.tyas.smartfarm.model.DataItem
import com.tyas.smartfarm.view.adapter.DailyForecastAdapter
import com.tyas.smartfarm.view.adapter.DailyWeather
import com.tyas.smartfarm.view.adapter.HourlyForecastAdapter
import com.tyas.smartfarm.view.pages.viewmodel.WeatherViewModel
import okio.AsyncTimeout.Companion.condition
import java.text.SimpleDateFormat
import java.util.Locale

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

        // Panggil fungsi fetchSimplifiedWeatherData
        weatherViewModel.fetchSimplifiedWeatherData()

        // Observasi data dari simplifiedWeatherData
        weatherViewModel.simplifiedWeatherData.observe(viewLifecycleOwner) { dataItems ->
            dataItems?.let { items ->
                // Map data items ke format yang sesuai untuk adapter
                val formattedData = items.map { item ->
                    DataItem(
                        item.date?.let { formatToDayName(it) }, // Konversi datetime menjadi nama hari jika diperlukan
                        item.summary,
                        weatherViewModel.getWeatherIconByDescription(item.summary) // Pastikan sesuai dengan properti yang dibutuhkan

                    )
                }

                // Atur adapter untuk RecyclerView
                val hourlyAdapter = HourlyForecastAdapter(formattedData)
                binding.hourlyForecastRecycler.layoutManager = LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                )
                binding.hourlyForecastRecycler.adapter = hourlyAdapter
            } ?: run {
                // Tampilkan pesan jika data kosong atau null
                Toast.makeText(requireContext(), "Tidak ada data cuaca sederhana", Toast.LENGTH_SHORT).show()
            }
        }




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
            val airQualityText = getString(R.string.air_quality_text, airQuality, airQualityIndex)
            binding.airQualityText.text = airQualityText
        }



        weatherViewModel.weatherIcon.observe(viewLifecycleOwner) { iconResId ->
            binding.weatherIcon.setImageResource(iconResId)
        }

        // Observasi data Per jam
        weatherViewModel.weatherData.observe(viewLifecycleOwner) { data ->
            //val filteredData = filterFirstDataPerDay(data) // Ambil data pertama per hari
            val dailyAdapter = DailyForecastAdapter(data.map {
                DailyWeather(
                    it.datetime, // Konversi datetime menjadi nama hari
                    it.weather_desc,
                    "${it.temperature}°",
                    weatherViewModel.getWeatherIconByDescription(it.weather_desc)// Panggil fungsi dari ViewModel
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

    private fun formatToDayName(datetime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // "EEEE" menghasilkan nama hari penuh
        return try {
            val date = inputFormat.parse(datetime)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
