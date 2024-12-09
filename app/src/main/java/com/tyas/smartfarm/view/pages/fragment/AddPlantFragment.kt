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
import com.google.firebase.auth.FirebaseAuth
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentAddPlantBinding
import com.tyas.smartfarm.model.Plant
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

        // Inisialisasi ViewModel
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        // Set hint untuk tanggal tanam
        binding.etDate.hint = "Pilih Tanggal Tanam"

        // Setup spinner kategori
        val categories = resources.getStringArray(R.array.category_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Event untuk memilih tanggal
        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // Event untuk menyimpan data tanaman
        binding.btnSave.setOnClickListener {
            savePlantData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observasi success dan error dari ViewModel
        plantViewModel.success.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addPlantFragment_to_plantFragment)
        }

        plantViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
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

        // Validasi form input
        if (plantName.isEmpty() || description.isEmpty() || plantDate.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi tambahan (contoh: nama tanaman minimal 3 karakter)
        if (plantName.length < 3) {
            Toast.makeText(requireContext(), "Nama tanaman terlalu pendek!", Toast.LENGTH_SHORT).show()
            return
        }

        // Dapatkan UID pengguna yang sedang login
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "User belum login", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat objek Plant dengan UID
        val newPlant = Plant(
            userId = currentUserId, // Tambahkan UID ke objek Plant
            name = plantName,
            description = description,
            category = category,
            plantingDate = plantDate
        )

        // Menyimpan data ke Firebase melalui ViewModel
        plantViewModel.insertPlant(newPlant)
    }
}
