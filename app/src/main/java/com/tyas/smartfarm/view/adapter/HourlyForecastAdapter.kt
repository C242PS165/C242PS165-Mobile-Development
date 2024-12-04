package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R

data class HourlyWeather(val time: String, val temperature: String, val iconResId: Int)

class HourlyForecastAdapter(private val hourlyData: List<HourlyWeather>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {

    inner class HourlyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.hourly_time)
        val weatherIconImageView: ImageView = view.findViewById(R.id.hourly_weather_icon)
        val temperatureTextView: TextView = view.findViewById(R.id.hourly_temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_forecast, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item = hourlyData[position]
        holder.timeTextView.text = item.time
        holder.temperatureTextView.text = item.temperature
        holder.weatherIconImageView.setImageResource(item.iconResId)
    }

    override fun getItemCount(): Int = hourlyData.size
}
