package com.tyas.smartfarm.view.pages.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentPlantBinding
import com.tyas.smartfarm.model.Article
import com.tyas.smartfarm.view.adapter.ArticleAdapter
import com.tyas.smartfarm.view.adapter.PlantAdapter
import com.tyas.smartfarm.view.pages.viewmodel.PlantViewModel
import com.tyas.smartfarm.api.ArticleApiService
import com.tyas.smartfarm.api.ApiClient
import kotlinx.coroutines.launch
import retrofit2.await

class PlantFragment : Fragment() {

    private var _binding: FragmentPlantBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantAdapter: PlantAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var articleApiService: ArticleApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("SmartFarmPrefs", Context.MODE_PRIVATE)
        val isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true)

        if (isFirstLogin) {
            showFarmerDialog()
            sharedPreferences.edit().putBoolean("isFirstLogin", false).apply()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvBroadcastMessage.text = "Check your plants! The forecast says rain tomorrow."

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        plantAdapter = PlantAdapter()
        binding.rvPlants.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = plantAdapter
        }

        plantViewModel.plantList.observe(viewLifecycleOwner, { plants ->
            if (plants.isNotEmpty()) {
                plantAdapter.submitList(plants)
            } else {
                binding.tvBroadcastMessage.text = "No plants found."
            }
        })

        // Handle loading state
        plantViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                binding.tvBroadcastMessage.text = "Loading plants..."
            } else {
                binding.tvBroadcastMessage.text = "Check your plants! The forecast says rain tomorrow."
            }
        })

        articleAdapter = ArticleAdapter(emptyList())
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }

        articleApiService = ApiClient.articleApiService
        fetchArticles()

        binding.btnAddPlant.setOnClickListener {
            findNavController().navigate(R.id.action_plantFragment_to_addPlantFragment)
        }
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            try {
                val apiKey = "sk-f78d6751878343fd87895"
                val response = articleApiService.getPlantArticles(apiKey).await()

                // Log respons API untuk memverifikasi data yang diterima
                Log.d("Article Data", "Received Response: $response")

                val articles = response.data.map { articleData ->
                    // Log artikel yang sedang diproses
                    Log.d("Article Data", "Mapping Article: $articleData")

                    // Menangani null atau ketidaksesuaian tipe pada sunlight
                    val sunlightList = articleData.sunlight?.filterIsInstance<String>() ?: listOf("Pencahayaan Tidak Diketahui")

                    // Menangani null pada defaultImage
                    val imageUrl = articleData.defaultImage?.regularUrl ?: "URL Gambar Tidak Diketahui"

                    // Membuat objek Article dengan data yang telah dipetakan
                    Article(
                        id = articleData.id,
                        commonName = articleData.commonName ?: "Nama Tanaman Tidak Diketahui",
                        scientificName = articleData.scientificName ?: listOf("Nama Ilmiah Tidak Diketahui"),
                        otherName = articleData.otherName ?: listOf("Nama Lain Tidak Diketahui"),
                        cycle = articleData.cycle ?: "Siklus Tidak Diketahui",
                        watering = articleData.watering ?: "Penyiraman Tidak Diketahui",
                        sunlight = sunlightList, // Memastikan List<String>
                        defaultImage = articleData.defaultImage // Memastikan defaultImage tidak null
                    )
                }

                // Mengupdate adapter dengan data artikel
                articleAdapter.setArticles(articles)
            } catch (e: Exception) {
                // Menangani error jika ada masalah
                Toast.makeText(requireContext(), "Failed to load articles: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Article Data", "Error fetching articles: ${e.message}", e)
            }
        }
    }

    private fun showFarmerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_farmer, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

        btnYes.setOnClickListener {
            Toast.makeText(requireContext(), "Selamat datang petani baru!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            Toast.makeText(requireContext(), "Semoga harimu menyenangkan!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
