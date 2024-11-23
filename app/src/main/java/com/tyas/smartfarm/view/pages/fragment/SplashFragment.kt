package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tyas.smartfarm.R
import com.tyas.smartfarm.util.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = DataStoreManager(requireContext())

        lifecycleScope.launch {
            val isLoggedIn = dataStoreManager.getLoginStatus().first()

            kotlinx.coroutines.delay(3000)

            if (isLoggedIn) {
                findNavController().navigate(R.id.action_splashFragment_to_plantFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_onBoardFragment)
            }
        }
    }
}
