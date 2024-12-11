package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R
import com.tyas.smartfarm.model.DataItem

class HourlyForecastAdapter(private val hourlyData: List<DataItem>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {

    inner class HourlyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.date_text)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weather_icon)
        val summaryTextView: TextView = view.findViewById(R.id.summary_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastAdapter.HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_forecast, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item = hourlyData[position]
        holder.summaryTextView.text = item.summary
        holder.dayTextView.text = item.date
        item.iconResId.let { holder.weatherIconImageView.setImageResource(it) }

        val poppinsRegular = ResourcesCompat.getFont(holder.itemView.context, R.font.poppins_regular)
        holder.summaryTextView.typeface = poppinsRegular

        val poppinsMedium = ResourcesCompat.getFont(holder.itemView.context, R.font.poppins_medium)
        holder.dayTextView.typeface = poppinsMedium
    }

    override fun getItemCount(): Int = hourlyData.size
}