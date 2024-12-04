package com.tyas.smartfarm.view.pages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tyas.smartfarm.model.database.Plant

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    // Referensi ke Realtime Database
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("plants")

    // LiveData untuk menyimpan daftar tanaman
    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>> get() = _plantList

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getAllPlants()
    }

    // Fungsi untuk menyimpan data tanaman ke Firebase
    fun insertPlant(plant: Plant) {
        val plantRef = databaseReference.push()
        plantRef.setValue(plant)
            .addOnSuccessListener {
                // Berhasil menyimpan data tanaman
            }
            .addOnFailureListener {
                // Gagal menyimpan data tanaman
            }
    }

    // Fungsi untuk mengambil semua tanaman dari Firebase
    fun getAllPlants() {
        _isLoading.value = true

        // Mengambil data tanaman dari Firebase
        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val plantList = snapshot.children.map { plantSnapshot ->
                    // Membaca data tanaman dari snapshot
                    val plant = plantSnapshot.getValue(Plant::class.java)
                        ?: Plant("", "", "", "", "") // Default value jika data kosong
                    plant.copy(id = plantSnapshot.key ?: "") // Menambahkan ID ke tanaman
                }
                _plantList.value = plantList // Menyimpan data ke LiveData
            }
            _isLoading.value = false // Selesai mengambil data, set loading false
        }.addOnFailureListener {
            _isLoading.value = false // Gagal mengambil data, set loading false
        }
    }
}
