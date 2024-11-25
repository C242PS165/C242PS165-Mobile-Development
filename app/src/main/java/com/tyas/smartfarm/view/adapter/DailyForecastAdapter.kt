package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R

data class DailyWeather(
    val day: String,
    val description: String,
    val temperature: String,
    val iconResId: Int
)

class DailyForecastAdapter(private val dailyData: List<DailyWeather>) :
    RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder>() {

    inner class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.daily_day)
        val weatherIconImageView: ImageView = view.findViewById(R.id.daily_weather_icon)
        val descriptionTextView: TextView = view.findViewById(R.id.daily_weather_description)
        val temperatureTextView: TextView = view.findViewById(R.id.daily_temperature_range)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val item = dailyData[position]
        holder.dayTextView.text = item.day
        holder.descriptionTextView.text = item.description
        holder.temperatureTextView.text = item.temperature
        holder.weatherIconImageView.setImageResource(item.iconResId)

        val poppinsRegular = ResourcesCompat.getFont(holder.itemView.context, R.font.poppins)
        holder.dayTextView.typeface = poppinsRegular
        holder.descriptionTextView.typeface = poppinsRegular
        holder.temperatureTextView.typeface = poppinsRegular

    }

    override fun getItemCount(): Int = dailyData.size
}
