package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentSettingsBinding
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

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
        val languages = listOf("English", "Bahasa Indonesia", "Español")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLanguage.adapter = adapter

        // Observe selected language
        settingsViewModel.selectedLanguage.observe(viewLifecycleOwner) { language ->
            binding.spinnerLanguage.setSelection(languages.indexOf(language))
        }

        // Handle language selection
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                lifecycleScope.launch {
                    settingsViewModel.updateLanguage(selectedLanguage)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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
