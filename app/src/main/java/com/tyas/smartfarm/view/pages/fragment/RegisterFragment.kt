package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tyas.smartfarm.R
import com.tyas.smartfarm.databinding.FragmentRegisterBinding
import com.tyas.smartfarm.model.AuthRepository
import com.tyas.smartfarm.view.pages.viewmodel.AuthState
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModel
import com.tyas.smartfarm.view.pages.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(com.google.firebase.auth.FirebaseAuth.getInstance()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbPassword1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.edRegisterPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.edRegisterPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Memindahkan kursor ke akhir teks saat tipe input berubah
            binding.edRegisterPassword.setSelection(binding.edRegisterPassword.text?.length ?: 0)
        }

        binding.btnDaftar.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (validateInput(email, password)) {
                registerUser(email, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }

        observeViewModel()
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerUser(email: String, password: String) {
        authViewModel.register(email, password)
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Idle -> setLoading(false)
                    is AuthState.Loading -> setLoading(true)
                    is AuthState.Success -> {
                        setLoading(false)
                        Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    }
                    is AuthState.Error -> {
                        setLoading(false)
                        Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnDaftar.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
