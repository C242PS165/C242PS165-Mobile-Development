package com.tyas.smartfarm.view.pages.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentAddPlantBinding
import com.tyas.smartfarm.model.database.Plant
import com.tyas.smartfarm.view.pages.viewmodel.PlantViewModel
import java.util.Calendar

class AddPlantFragment : Fragment() {

    private lateinit var binding: FragmentAddPlantBinding
    private lateinit var plantViewModel: PlantViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlantBinding.inflate(inflater, container, false)

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        binding.etDate.hint = "Pilih Tanggal Tanam"
        val categories = resources.getStringArray(R.array.category_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            savePlantData()
        }

        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDate.setText(date)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun savePlantData() {
        val plantName = binding.etPlantName.text.toString()
        val description = binding.etDescription.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val plantDate = binding.etDate.text.toString()

        // Validasi form
        if (plantName.isEmpty() || description.isEmpty() || plantDate.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat objek tanaman tanpa gambar
        val newPlant = Plant(
            name = plantName,
            description = description,
            category = category,
            plantingDate = plantDate
        )

        // Menyimpan data tanaman ke Firebase Realtime Database
        plantViewModel.insertPlant(newPlant)

        Toast.makeText(requireContext(), "Tanaman berhasil disimpan ke Firebase!", Toast.LENGTH_SHORT).show()

        // Navigasi kembali ke halaman PlantFragment setelah sukses
        findNavController().navigate(R.id.action_addPlantFragment_to_plantFragment)
    }
}
