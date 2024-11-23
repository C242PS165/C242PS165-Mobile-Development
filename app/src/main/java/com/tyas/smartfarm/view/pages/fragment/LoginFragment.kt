package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentLoginBinding
import com.tyas.smartfarm.model.AuthRepository
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModel
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(com.google.firebase.auth.FirebaseAuth.getInstance()))
    }

    private var hasNavigatedToMain = false // Prevent multiple navigations

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cek status login hanya saat pertama kali fragment dimulai
        lifecycleScope.launchWhenStarted {
            if (isUserLoggedIn()) {
                navigateToMain()
            }
        }

        binding.cbPassword2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.edPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.edPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Memindahkan kursor ke akhir teks saat tipe input berubah
            binding.edPassword.setSelection(binding.edPassword.text?.length ?: 0)
        }

        // Tombol Login
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        // Tombol Daftar
        binding.tvDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Observasi ViewModel untuk login
        observeViewModel()
    }

    private suspend fun isUserLoggedIn(): Boolean {
        val dataStoreManager = DataStoreManager(requireContext())
        val isLoggedIn = dataStoreManager.getLoginStatus().first()
        Log.d("LoginFragment", "Status login: $isLoggedIn")
        return isLoggedIn
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email dan password harus diisi!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Format email tidak valid!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        authViewModel.login(email, password)
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            authViewModel.authState.collect { state ->
                when (state) {
                    is com.tyas.smartfarm.view.pages.viewmodel.AuthState.Idle -> setLoading(false)
                    is com.tyas.smartfarm.view.pages.viewmodel.AuthState.Loading -> setLoading(true)
                    is com.tyas.smartfarm.view.pages.viewmodel.AuthState.Success -> {
                        setLoading(false)
                        saveLoginStatus(true)
                        navigateToMain()
                    }
                    is com.tyas.smartfarm.view.pages.viewmodel.AuthState.Error -> {
                        setLoading(false)
                        Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val dataStoreManager = DataStoreManager(requireContext())
        lifecycleScope.launch {
            dataStoreManager.setLoginStatus(isLoggedIn)
        }
    }

    private fun navigateToMain() {
        if (!hasNavigatedToMain) { // Prevent multiple navigations
            hasNavigatedToMain = true
            findNavController().navigate(R.id.action_loginFragment_to_plantFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
