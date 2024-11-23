package com.tyas.smartfarm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tyas.smartfarm.R
import com.tyas.smartfarm.model.Plant

class PlantAdapter(private val plants: List<Plant>) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantImage: ImageView = view.findViewById(R.id.iv_plant_image)
        val plantName: TextView = view.findViewById(R.id.tv_plant_name)
        val plantStatus: TextView = view.findViewById(R.id.tv_plant_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.plantImage.setImageResource(plant.imageResId)
        holder.plantName.text = plant.name
        holder.plantStatus.text = plant.status
    }

    override fun getItemCount(): Int = plants.size
}
