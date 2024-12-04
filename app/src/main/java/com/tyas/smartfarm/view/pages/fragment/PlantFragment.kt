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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentPlantBinding
import com.tyas.smartfarm.model.Article
import com.tyas.smartfarm.view.adapter.ArticleAdapter
import com.tyas.smartfarm.view.adapter.PlantAdapter
import com.tyas.smartfarm.view.pages.viewmodel.PlantViewModel
import androidx.navigation.fragment.findNavController

class PlantFragment : Fragment() {

    private var _binding: FragmentPlantBinding? = null
    private val binding get() = _binding!!

    private lateinit var plantAdapter: PlantAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var plantViewModel: PlantViewModel

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

        // Initialize ViewModel
        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)

        // Initialize Adapter
        plantAdapter = PlantAdapter()
        binding.rvPlants.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = plantAdapter
        }

        // Observe plant list
        plantViewModel.plantList.observe(viewLifecycleOwner, { plants ->
            if (plants.isNotEmpty()) {
                plantAdapter.submitList(plants) // no need to convert to MutableList anymore
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

        // Initialize Article RecyclerView
        articleAdapter = ArticleAdapter(getDummyArticles())
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }

        // Navigate to AddPlant page
        binding.btnAddPlant.setOnClickListener {
            findNavController().navigate(R.id.action_plantFragment_to_addPlantFragment)
        }
    }

    private fun getDummyArticles() = listOf(
        Article("Article 1", "This is a description", R.drawable.article_dummy),
        Article("Article 2", "This is another description", R.drawable.dummy),
        Article("Article 3", "Description for article", R.drawable.dummy),
        Article("Article 4", "More details on articles", R.drawable.dummy)
    )

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
