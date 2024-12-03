package com.tyas.smartfarm.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tyas.smartfarm.model.database.Plant  // Pastikan ini yang diimpor!

@Dao
interface PlantDao {
    @Insert
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * FROM plants")
    fun getAllPlants(): LiveData<List<Plant>>

    @Delete
    suspend fun deletePlant(plant: Plant)

    @Update
    suspend fun updatePlant(plant: Plant)
}
