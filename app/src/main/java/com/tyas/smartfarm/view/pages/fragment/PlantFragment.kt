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

        // Inisialisasi ApiService
        articleApiService = ApiClient.articleApiService

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        // Set up RecyclerView untuk tanaman
        plantAdapter = PlantAdapter()
        binding.rvPlants.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = plantAdapter
            visibility = View.GONE // Sembunyikan secara default
        }

        // Set up RecyclerView untuk artikel
        articleAdapter = ArticleAdapter(emptyList())
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
            visibility = View.GONE // Sembunyikan secara default
        }

        // Observasi daftar tanaman
        plantViewModel.plantList.observe(viewLifecycleOwner) { plants ->
            if (plants.isNotEmpty()) {
                plantAdapter.submitList(plants)
                binding.rvPlants.visibility = View.VISIBLE
                binding.tvEmptyPlaceholder.visibility = View.GONE
            } else {
                binding.rvPlants.visibility = View.GONE
                binding.tvEmptyPlaceholder.visibility = View.VISIBLE
            }
        }

        // Observasi status loading untuk tanaman
        plantViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.rvPlants.visibility = View.GONE
                binding.tvEmptyPlaceholder.visibility = View.GONE
            }
        }

        // Panggil fetchArticles untuk memuat artikel
        fetchArticles()

        // Navigasi ke halaman tambah tanaman
        binding.btnAddPlant.setOnClickListener {
            findNavController().navigate(R.id.action_plantFragment_to_addPlantFragment)
        }
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            try {
                // Tampilkan ProgressBar untuk artikel dan sembunyikan RecyclerView
                binding.progressBarArticles.visibility = View.VISIBLE
                binding.rvArticles.visibility = View.GONE

                val apiKey = "sk-f78d6751878343fd87895"
                val response = articleApiService.getPlantArticles(apiKey).await()

                val articles = response.data.map { articleData ->
                    Article(
                        id = articleData.id,
                        commonName = articleData.commonName ?: "Unknown Name",
                        scientificName = articleData.scientificName ?: listOf("Unknown Scientific Name"),
                        otherName = articleData.otherName ?: listOf("Unknown Other Name"),
                        cycle = articleData.cycle ?: "Unknown Cycle",
                        watering = articleData.watering ?: "Unknown Watering",
                        sunlight = articleData.sunlight ?: listOf("Unknown Sunlight"),
                        defaultImage = articleData.defaultImage
                    )
                }

                // Tampilkan artikel di RecyclerView
                articleAdapter.setArticles(articles)
                binding.rvArticles.visibility = View.VISIBLE
            } catch (e: Exception) {
                // Tampilkan pesan error jika gagal memuat artikel
                Toast.makeText(requireContext(), "Failed to load articles: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Article Data", "Error fetching articles: ${e.message}", e)
            } finally {
                // Sembunyikan ProgressBar setelah proses selesai
                binding.progressBarArticles.visibility = View.GONE
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
            Toast.makeText(requireContext(), "Welcome, new farmer!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            Toast.makeText(requireContext(), "Have a nice day!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
