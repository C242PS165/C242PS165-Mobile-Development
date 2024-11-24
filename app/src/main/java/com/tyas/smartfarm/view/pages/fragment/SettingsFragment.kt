package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tyas.smartfarm.databinding.FragmentSettingsBinding
import com.tyas.smartfarm.util.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStoreManager = DataStoreManager(requireContext())

        // Set state awal dari switch berdasarkan data di DataStore
        lifecycleScope.launch {
            binding.switchNotification.isChecked = dataStoreManager.isNotificationEnabled().first()
        }

        // Event Listener untuk Notifikasi
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                dataStoreManager.setNotificationEnabled(isChecked)
                val message = if (isChecked) {
                    "Notifikasi diaktifkan"
                } else {
                    "Notifikasi dinonaktifkan"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke Tentang Aplikasi
        binding.btnAboutApp.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "SmartFarm v1.0\nDibuat untuk membantu petani Indonesia!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
