package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
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
        viewPager.setPageTransformer { page, position ->
            page.translationX = -position * page.width / 2 // Efek translasi
            page.alpha = 0.05f + (1 - kotlin.math.abs(position)) // Efek transparansi
            page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f // Efek skala
        }

        val indicator: CircleIndicator3 = view.findViewById(R.id.indicator)

        // Data untuk carousel
        val items = listOf(
            CarouselItem(
                R.drawable.farmer3,
                "We are here to help you",
                "Providing guidance and solutions for farmers to enhance productivity and efficiency"
            ),
            CarouselItem(
                R.drawable.cloudy,
                "To see latest weather",
                "Stay updated with real-time weather forecasts and predictions for your farming needs"
            ),
            CarouselItem(
                R.drawable.farmer2,
                "Planting advice and\nPlanting procedures",
                "Explore tailored planting tips and detailed step-by-step farming procedures"
            )
        )

        val adapter = CarouselAdapter(items)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        // Tombol "Continue" untuk swipe terakhir
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        view.findViewById<TextView>(R.id.continue_button).setOnClickListener {
            findNavController().navigate(R.id.action_carouselFragment_to_onBoardFragment, null, options)
        }

        // Callback untuk halaman terakhir
        val continueButton = view.findViewById<TextView>(R.id.continue_button)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == items.size - 1) {
                    continueButton.visibility = View.VISIBLE
                    continueButton.alpha = 0f
                    continueButton.animate().alpha(1f).setDuration(300).start()
                } else {
                    continueButton.animate().alpha(0f).setDuration(300).withEndAction {
                        continueButton.visibility = View.GONE
                    }.start()
                }
            }
        })



    }
}
