package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tyas.smartfarm.databinding.FragmentPlantCareBinding
import com.tyas.smartfarm.view.pages.viewmodel.PlantCareViewModel

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

        if (plantId != null && userId != null) {
            viewModel.fetchPlantDetails(userId!!, plantId!!)
        } else {
            Toast.makeText(requireContext(), "Data tanaman tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
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
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
