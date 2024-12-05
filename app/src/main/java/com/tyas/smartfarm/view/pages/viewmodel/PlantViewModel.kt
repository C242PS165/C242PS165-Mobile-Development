package com.tyas.smartfarm.view.pages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tyas.smartfarm.model.Plant

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("plants")

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>> get() = _plantList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()

    private val _success = MutableLiveData<String>()

    init {
        getAllPlants()
    }

    fun insertPlant(plant: Plant) {
        val plantRef = databaseReference.push()
        plantRef.setValue(plant)
            .addOnSuccessListener {
                _success.value = "Catatan Tanaman Berhasil Dibuat"
            }
            .addOnFailureListener {
                _error.value = "Failed to add plant"
            }
    }

    fun getAllPlants() {
        _isLoading.value = true

        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val plantList = snapshot.children.mapNotNull { plantSnapshot ->
                    val plant = plantSnapshot.getValue(Plant::class.java)
                    plant?.copy(id = plantSnapshot.key ?: "")
                }
                _plantList.value = plantList
            } else {
                _plantList.value = emptyList()
            }
            _isLoading.value = false
        }.addOnFailureListener {
            _isLoading.value = false
            _error.value = "Failed to load plants"
        }
    }
}
