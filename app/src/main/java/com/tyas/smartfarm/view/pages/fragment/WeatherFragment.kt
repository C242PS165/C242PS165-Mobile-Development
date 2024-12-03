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

        // Observasi LiveData dari ViewModel
        weatherViewModel.weatherData.observe(viewLifecycleOwner) { data ->
            val dailyAdapter = DailyForecastAdapter(data.map {
                DailyWeather(it.datetime, it.weather_desc, "${it.temperature}°", R.drawable.ic_cloudy)
            })
            binding.dailyForecastRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.dailyForecastRecycler.adapter = dailyAdapter
        }

        weatherViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        weatherViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        // Panggil data cuaca
        weatherViewModel.fetchWeatherData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
