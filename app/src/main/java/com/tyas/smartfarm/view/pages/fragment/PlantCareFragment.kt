package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.tyas.smartfarm.databinding.FragmentPlantCareBinding
import com.tyas.smartfarm.view.pages.viewmodel.PlantCareViewModel
import android.app.AlertDialog
import com.tyas.smartfarm.view.pages.customview.PlantReminderBottomSheet

class PlantCareFragment : Fragment() {

    private var _binding: FragmentPlantCareBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlantCareViewModel
    private var plantId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            plantId = it.getString("plantId")
            userId = it.getString("userId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlantCareViewModel::class.java)

        // Pastikan plantId dan userId ada sebelum fetch data
        if (!plantId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
            viewModel.fetchPlantDetails(userId!!, plantId!!)
        } else {
            Toast.makeText(requireContext(), "Data tanaman tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()

        // Set listener untuk tombol Update
        binding.buttonUpdate.setOnClickListener {
            updatePlantDescription()
        }

        // Set listener untuk tombol Delete
        binding.buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Tambahkan listener untuk tombol "Atur Pengingat"
        binding.buttonSetReminder.setOnClickListener {
            // Tampilkan BottomSheet untuk pengingat
            val bottomSheet = PlantReminderBottomSheet()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    private fun observeViewModel() {
        viewModel.plant.observe(viewLifecycleOwner) { plant ->
            binding.textViewPlantName.text = plant.name
            binding.textViewPlantCategory.text = plant.category
            binding.editTextPlantDescription.setText(plant.description)
            binding.textViewPlantingDate.text = plant.plantingDate
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarPlant.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePlantDescription() {
        val newDescription = binding.editTextPlantDescription.text.toString()

        // Validasi input (misalnya, memastikan deskripsi tidak kosong)
        if (newDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // Konfirmasi sebelum mengupdate
        showUpdateConfirmationDialog(newDescription)
    }

    private fun showUpdateConfirmationDialog(newDescription: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Deskripsi")
            .setMessage("Apakah kamu yakin ingin memperbarui deskripsi tanaman?")
            .setPositiveButton("Yes") { dialog, _ ->
                performUpdate(newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.create().show()
    }

    private fun performUpdate(newDescription: String) {
        // Pastikan plantId dan userId tidak null sebelum melanjutkan
        if (!plantId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
            // Ambil reference ke data tanaman di Firebase
            val database = FirebaseDatabase.getInstance()
            val plantRef = database.getReference("plants/$userId/$plantId")
            val userPlantRef = database.getReference("users/$userId/plants/$plantId")

            // Update data tanaman
            val updates = mapOf("description" to newDescription)

            // Update pada kedua referensi (plants dan users)
            plantRef.updateChildren(updates)
                .addOnSuccessListener {
                    userPlantRef.updateChildren(updates)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Deskripsi tanaman berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            binding.editTextPlantDescription.setText(newDescription)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal memperbarui deskripsi tanaman di data pengguna", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal memperbarui deskripsi tanaman", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hapus Tanaman")
            .setMessage("Apakah kamu yakin ingin menghapus catatan tanaman ini?")
            .setPositiveButton("Yes") { dialog, _ ->
                deletePlant()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.create().show()
    }

    private fun deletePlant() {
        // Pastikan plantId dan userId tidak null sebelum melanjutkan
        if (!plantId.isNullOrEmpty() && !userId.isNullOrEmpty()) {
            val database = FirebaseDatabase.getInstance()
            val plantRef = database.getReference("plants/$userId/$plantId")
            val userPlantRef = database.getReference("users/$userId/plants/$plantId")

            plantRef.removeValue()
                .addOnSuccessListener {
                    userPlantRef.removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Tanaman berhasil dihapus", Toast.LENGTH_SHORT).show()

                            // Kembali ke halaman sebelumnya setelah berhasil dihapus
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Gagal menghapus data tanaman dari pengguna", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal menghapus data tanaman", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
