package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentWeatherBinding
import com.tyas.smartfarm.view.adapter.DailyForecastAdapter
import com.tyas.smartfarm.view.adapter.DailyWeather

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dailyData = listOf(
            DailyWeather("Hari ini", "Kabut asap", "33° / 25°", R.drawable.ic_weather),
            DailyWeather("Besok", "Badai petir", "33° / 25°", R.drawable.ic_weather),
            DailyWeather("Sabtu", "Badai petir", "34° / 25°", R.drawable.ic_weather),
            DailyWeather("Minggu", "Badai petir", "32° / 25°", R.drawable.ic_weather),
            DailyWeather("Senin", "Badai petir", "32° / 25°", R.drawable.ic_weather),
            DailyWeather("Selasa", "Badai petir", "32° / 25°", R.drawable.ic_weather),
            DailyWeather("Rabu", "Badai petir", "32° / 25°", R.drawable.ic_weather)
        )

        // Setup Daily Forecast RecyclerView
        val dailyAdapter = DailyForecastAdapter(dailyData)
        binding.dailyForecastRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.dailyForecastRecycler.adapter = dailyAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
