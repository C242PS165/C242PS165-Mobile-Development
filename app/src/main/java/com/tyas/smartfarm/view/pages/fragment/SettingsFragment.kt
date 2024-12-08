package com.tyas.smartfarm.view.pages.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tyas.smartfarm.databinding.FragmentSettingsBinding
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.view.pages.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels {
        val dataStoreManager = DataStoreManager(requireContext())
        SettingsViewModel.Factory(dataStoreManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe notifications setting
        settingsViewModel.notificationsEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.switchNotifications.isChecked = isEnabled
        }

        // Handle notification toggle
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.updateNotificationsEnabled(isChecked)
            Toast.makeText(requireContext(), "Notifications ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }

        // Setup language spinner
        val languages = listOf("English", "Bahasa Indonesia")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.btnLanguageSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)

            Toast.makeText(
                requireContext(),
                "Please select your preferred language in device settings.",
                Toast.LENGTH_SHORT
            ).show()
        }



        // Logout button
        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logged out!", Toast.LENGTH_SHORT).show()
            // Handle logout logic here
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
