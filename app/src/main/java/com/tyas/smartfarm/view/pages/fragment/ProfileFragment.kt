package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentProfileBinding
import com.tyas.smartfarm.model.AuthRepository
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModel
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModelFactory
import com.tyas.smartfarm.view.pages.viewmodel.ProfileViewModel
import com.tyas.smartfarm.model.db.User
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(com.google.firebase.auth.FirebaseAuth.getInstance()))
    }

    // ViewModel untuk Room Database
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observasi data user untuk menampilkan di UI
        observeUserData()

        // Navigasi ke SettingsFragment
        binding.btnSettings.setOnClickListener {
            navigateToSettings()
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        // Tombol Edit Profile
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }
    }

    private fun observeUserData() {
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvUsername.text = it.username
                binding.tvEmail.text = it.email
                binding.tvBio.text = it.bio
                binding.tvPhoneNumber.text = it.phoneNumber
            }
        }
    }

    private fun showEditProfileDialog() {
        // Inflate layout dialog
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)

        // Referensi komponen dari dialog
        val etUsername = dialogView.findViewById<EditText>(R.id.et_username)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)
        val etBio = dialogView.findViewById<EditText>(R.id.et_bio)
        val etPhoneNumber = dialogView.findViewById<EditText>(R.id.et_phoneNumber)
        val btnSave = dialogView.findViewById<MaterialButton>(R.id.btn_save)

        // Pre-fill data user dari database
        profileViewModel.user.value?.let {
            etUsername.setText(it.username)
            etEmail.setText(it.email)
            etBio.setText(it.bio)
            etPhoneNumber.setText(it.phoneNumber)
        }

        // Buat dan tampilkan dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Tombol Simpan
        btnSave.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val bio = etBio.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()

            if (username.isNotBlank() && email.isNotBlank()) {
                // Simpan data ke database
                val updatedUser = User(username = username, email = email, bio = bio, phoneNumber = phoneNumber)
                profileViewModel.saveUser(updatedUser)

                // Tutup dialog
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Username dan Email harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
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