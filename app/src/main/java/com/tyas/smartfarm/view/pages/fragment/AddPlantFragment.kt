package com.tyas.smartfarm.view.pages.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentAddPlantBinding
import java.util.*

class AddPlantFragment : Fragment() {

    private lateinit var binding: FragmentAddPlantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        binding = FragmentAddPlantBinding.inflate(inflater, container, false)

        // Set default hint for Tanggal Tanam
        binding.etDate.hint = "Pilih Tanggal Tanam"

        // Setup Spinner for kategori tanaman
        val categories = resources.getStringArray(R.array.category_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Listener for Tanggal Tanam input
        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // Listener for the save button
        binding.btnSave.setOnClickListener {
            savePlantData()
        }

        return binding.root
    }

    // Function to display the DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Display the selected date in the TextInputEditText
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

        if (plantName.isEmpty() || description.isEmpty() || plantDate.isEmpty()) {
            return
        }

        // Here you can save the plant data (print for now)
        println("Nama Tanaman: $plantName")
        println("Deskripsi: $description")
        println("Kategori: $category")
        println("Tanggal Penanaman: $plantDate")
    }
}
