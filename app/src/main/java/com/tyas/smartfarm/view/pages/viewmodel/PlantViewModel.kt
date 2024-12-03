package com.tyas.smartfarm.view.pages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.model.database.Plant
import com.tyas.smartfarm.model.database.PlantDatabase
import kotlinx.coroutines.launch

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val plantDao = PlantDatabase.getDatabase(application).plantDao()

    fun getAllPlants(): LiveData<List<Plant>> {
        return plantDao.getAllPlants()
    }

    fun insertPlant(plant: Plant) {
        viewModelScope.launch {
            plantDao.insertPlant(plant)
        }
    }
}
