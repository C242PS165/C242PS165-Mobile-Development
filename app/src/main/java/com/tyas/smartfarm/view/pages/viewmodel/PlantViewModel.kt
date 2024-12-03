package com.tyas.smartfarm.view.pages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.model.database.Plant // Menggunakan Plant yang tepat
import com.tyas.smartfarm.model.database.PlantDatabase
import kotlinx.coroutines.launch

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val plantDao = PlantDatabase.getDatabase(application).plantDao()

    // Fungsi ini sekarang menggunakan com.tyas.smartfarm.model.database.Plant
    fun insertPlant(plant: Plant) {
        viewModelScope.launch {
            plantDao.insertPlant(plant)
        }
    }
}
