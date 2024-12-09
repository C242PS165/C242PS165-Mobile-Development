package com.tyas.smartfarm.view.pages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.tyas.smartfarm.model.Plant

class PlantCareViewModel : ViewModel() {

    private val _plant = MutableLiveData<Plant>()
    val plant: LiveData<Plant> get() = _plant

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Fetch details of a plant
    fun fetchPlantDetails(userId: String, plantId: String) {
        _isLoading.value = true
        val database = FirebaseDatabase.getInstance()
        val plantRef = database.getReference("users/$userId/plants/$plantId")

        plantRef.get().addOnSuccessListener { snapshot ->
            val plant = snapshot.getValue(Plant::class.java)
            if (plant != null) {
                _plant.value = plant
                _errorMessage.value = null
            } else {
                _errorMessage.value = "Tanaman tidak ditemukan"
            }
            _isLoading.value = false
        }.addOnFailureListener {
            _errorMessage.value = "Gagal memuat data tanaman: ${it.message}"
            _isLoading.value = false
        }
    }

    // Delete plant from Firebase
    fun deletePlantFromDatabase(userId: String, plantId: String) {
        _isLoading.value = true
        val database = FirebaseDatabase.getInstance()

        val plantRef = database.getReference("plants/$userId/$plantId")
        val userPlantRef = database.getReference("users/$userId/plants/$plantId")

        // Remove plant data from the "plants" and "users" paths in Firebase
        plantRef.removeValue()
            .addOnSuccessListener {
                userPlantRef.removeValue()
                    .addOnSuccessListener {
                        // Data berhasil dihapus
                        _isLoading.value = false
                        _errorMessage.value = null
                    }
                    .addOnFailureListener {
                        _errorMessage.value = "Gagal menghapus data tanaman dari pengguna"
                        _isLoading.value = false
                    }
            }
            .addOnFailureListener {
                _errorMessage.value = "Gagal menghapus data tanaman"
                _isLoading.value = false
            }
    }
}
