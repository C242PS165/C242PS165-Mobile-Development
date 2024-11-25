package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentProfileBinding
import com.tyas.smartfarm.model.AuthRepository
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModel
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(com.google.firebase.auth.FirebaseAuth.getInstance()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke SettingsFragment
        binding.btnSettings.setOnClickListener {
            navigateToSettings()
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.settingsFragment)
    }

    private fun logoutUser() {
        val dataStoreManager = DataStoreManager(requireContext())

        lifecycleScope.launch {
            dataStoreManager.setLoginStatus(false)

            authViewModel.logout()

            findNavController().navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )

            Toast.makeText(requireContext(), getString(R.string.logout_berhasil), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
