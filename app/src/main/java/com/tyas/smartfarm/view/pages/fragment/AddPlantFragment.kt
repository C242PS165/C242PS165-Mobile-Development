package com.tyas.smartfarm.view.pages.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentAddPlantBinding
import com.tyas.smartfarm.view.pages.viewmodel.PlantViewModel
import java.util.Calendar

class AddPlantFragment : Fragment() {

    private lateinit var binding: FragmentAddPlantBinding
    private lateinit var plantViewModel: PlantViewModel

    private val pickImageRequestCode = 100

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

        // Listener for Tanggal Tanam input
        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // Listener for the save button
        binding.btnSave.setOnClickListener {
            savePlantData()
        }

        // Listener for the upload image button
        binding.btnUploadImage.setOnClickListener {
            openGalleryForImage()
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
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDate.setText(date)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    // Function to open gallery and choose an image
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*" // Hanya file gambar
        startActivityForResult(intent, pickImageRequestCode)
    }

    // Handle result from gallery pick
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImageRequestCode) {
            val imageUri = data?.data // URI gambar yang dipilih

            // Tampilkan gambar yang dipilih ke ImageView menggunakan Glide atau Picasso
            Glide.with(this)
                .load(imageUri)
                .into(binding.ivPlantImage) // Menampilkan gambar di ImageView
        }
    }

    // Function to save plant data to Room Database
    private fun savePlantData() {
        val plantName = binding.etPlantName.text.toString()
        val description = binding.etDescription.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val plantDate = binding.etDate.text.toString()

        // Log data untuk memastikan data yang dikirim valid
        Log.d("AddPlantFragment", "Saving Plant: $plantName, $category, $description, $plantDate")

        // Pastikan field tidak kosong
        if (plantName.isEmpty() || description.isEmpty() || plantDate.isEmpty()) {
            Log.d("AddPlantFragment", "Fields are empty, cannot save plant.")
            // Bisa beri feedback ke user
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat objek Plant dengan model database
        val newPlant = com.tyas.smartfarm.model.database.Plant(
            name = plantName,
            description = description,
            category = category,
            plantDate = plantDate
        )

        // Log sebelum menyimpan ke database
        Log.d("AddPlantFragment", "Saving Plant to Database: $newPlant")

        // Simpan ke database
        plantViewModel.insertPlant(newPlant)

        // Tampilkan pesan berhasil
        Toast.makeText(requireContext(), "Tanaman Berhasil dibuat!", Toast.LENGTH_SHORT).show()

        // Pastikan setelah menyimpan data, halaman Plant diperbarui
        Log.d("AddPlantFragment", "Plant saved successfully.")

        // Navigasi kembali ke halaman Plant
        findNavController().navigate(R.id.action_addPlantFragment_to_plantFragment)
    }
}
