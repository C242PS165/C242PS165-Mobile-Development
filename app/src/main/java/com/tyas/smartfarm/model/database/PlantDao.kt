package com.tyas.smartfarm.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {
    @Insert
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * FROM plants")
    fun getAllPlants(): LiveData<List<Plant>>
}
