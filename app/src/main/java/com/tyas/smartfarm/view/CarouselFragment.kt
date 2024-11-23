package com.tyas.smartfarm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tyas.smartfarm.R
import com.tyas.smartfarm.util.DataStoreManager
import com.tyas.smartfarm.view.adapter.CarouselAdapter
import com.tyas.smartfarm.view.adapter.CarouselItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3

class CarouselFragment : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_carousel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi DataStoreManager
        dataStoreManager = DataStoreManager(requireContext())

        // Cek apakah user sudah login
        lifecycleScope.launch {
            val isLoggedIn = dataStoreManager.getLoginStatus().first()
            if (isLoggedIn) {
                // Jika sudah login, langsung navigasi ke PlantFragment
                findNavController().navigate(R.id.action_carouselFragment_to_plantFragment)
            }
        }

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val indicator: CircleIndicator3 = view.findViewById(R.id.indicator)

        // Data untuk carousel
        val items = listOf(
            CarouselItem(R.drawable.farmer3, "We are here to help you"),
            CarouselItem(R.drawable.cloudy, "To see latest weather"),
            CarouselItem(R.drawable.farmer2, "Planting advice/planting procedures")
        )

        val adapter = CarouselAdapter(items)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        // Tombol "Continue" untuk swipe terakhir
        view.findViewById<TextView>(R.id.continue_button).setOnClickListener {
            findNavController().navigate(R.id.action_carouselFragment_to_onBoardFragment)
        }

        // Callback untuk halaman terakhir
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == items.size - 1) {
                    // Tombol "Continue" hanya terlihat di halaman terakhir
                    view.findViewById<TextView>(R.id.continue_button).visibility = View.VISIBLE
                } else {
                    view.findViewById<TextView>(R.id.continue_button).visibility = View.GONE
                }
            }
        })
    }
}
