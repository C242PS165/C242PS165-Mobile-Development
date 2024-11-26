package com.tyas.smartfarm.view.pages.fragment

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentPlantBinding
import com.tyas.smartfarm.model.Article
import com.tyas.smartfarm.model.Plant
import com.tyas.smartfarm.view.adapter.ArticleAdapter
import com.tyas.smartfarm.view.adapter.PlantAdapter

class PlantFragment : Fragment() {


    private var _binding: FragmentPlantBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantAdapter: PlantAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tangani tombol "Back" untuk keluar aplikasi
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Keluar dari aplikasi
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Gunakan View Binding untuk menghubungkan layout
        _binding = FragmentPlantBinding.inflate(inflater, container, false)


        // Cek apakah ini login pertama
        val sharedPreferences = requireActivity().getSharedPreferences("SmartFarmPrefs", Context.MODE_PRIVATE)
        val isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true)

        if (isFirstLogin) {
            // Tampilkan dialog jika login pertama
            showFarmerDialog()

            // Update status menjadi bukan login pertama
            sharedPreferences.edit().putBoolean("isFirstLogin", false).apply()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Text Broadcast Section
        binding.tvBroadcastMessage.text = "Check your plants! The forecast says rain tomorrow."

        // Initialize Plant RecyclerView
        plantAdapter = PlantAdapter(getDummyPlants())
        binding.rvPlants.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = plantAdapter
        }

        // Initialize Article RecyclerView
        articleAdapter = ArticleAdapter(getDummyArticles())
        binding.rvArticles.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }

        // Add Plant Button Action
        binding.btnAddPlant.setOnClickListener {
            findNavController().navigate(R.id.action_plantFragment_to_addPlantFragment)
            Toast.makeText(requireContext(), "Add Plant clicked!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDummyPlants() = listOf(
        Plant("Spinach", "Can be harvested", R.drawable.dummy),
        Plant("Lettuce", "Needs more attention", R.drawable.dummy),
        Plant("Cucumber", "Needs water - 250 ml", R.drawable.dummy)
    )

    private fun getDummyArticles() = listOf(
        Article("Article 1", "This issasasaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa a description", R.drawable.dummy),
        Article("Article 2", "This is a description", R.drawable.dummy),
        Article("Article 3", "Another description", R.drawable.dummy),
        Article("Article 4", "Another description", R.drawable.dummy),
        Article("Article 5", "Another description", R.drawable.dummy),
        Article("Article 6", "Another description", R.drawable.dummy),
        Article("Article 7", "Another description", R.drawable.dummy),
        Article("Article 8", "Another description", R.drawable.dummy),
        Article("Article 9", "Another description", R.drawable.dummy),
        Article("Article 10", "Another description", R.drawable.dummy),
        Article("Article 11", "Another description", R.drawable.dummy)
    )

    private fun showFarmerDialog() {
        // Inflate layout custom untuk dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_farmer, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // Bind tombol dari layout custom
        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

        // Aksi tombol "Iya"
        btnYes.setOnClickListener {
            Toast.makeText(requireContext(), "Selamat datang petani baru!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Aksi tombol "Tidak"
        btnNo.setOnClickListener {
            Toast.makeText(requireContext(), "Semoga harimu menyenangkan!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Tampilkan dialog
        dialog.show()
    }
}
