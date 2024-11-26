package com.tyas.smartfarm.view.pages.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.button.MaterialButton
import com.tyas.smartfarm.R

class OnBoardFragment : Fragment() {

    private lateinit var imgOpacity: ImageView
    private lateinit var tvTeks1: TextView
    private lateinit var tvTeks2: TextView
    private lateinit var btnToLogin: MaterialButton
    private lateinit var btnToRegister: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board, container, false)

        // Inisialisasi elemen layout
        imgOpacity = view.findViewById(R.id.img_opacity)
        tvTeks1 = view.findViewById(R.id.tv_teks1)
        tvTeks2 = view.findViewById(R.id.tv_teks2)
        btnToLogin = view.findViewById(R.id.btn_to_login)
        btnToRegister = view.findViewById(R.id.btn_to_register)

        // Setel visibilitas awal elemen ke GONE
        imgOpacity.visibility = View.GONE
        tvTeks1.visibility = View.GONE
        tvTeks2.visibility = View.GONE
        btnToLogin.visibility = View.GONE
        btnToRegister.visibility = View.GONE

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimations()

        btnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardFragment_to_loginFragment, null, options)
        }

        btnToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardFragment_to_registerFragment, null, options )
        }
    }

    private fun startAnimations() {
        // Animasi fade-in dan fade-out untuk gambar opacity
        imgOpacity.visibility = View.VISIBLE
        imgOpacity.alpha = 0f
        imgOpacity.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .withEndAction {
                imgOpacity.animate()
                    .alpha(0f)
                    .setDuration(1000)
                    .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                    .withEndAction {
                        imgOpacity.visibility = View.GONE

                        // Animasi teks masuk dari kiri ke kanan
                        tvTeks1.visibility = View.VISIBLE
                        tvTeks1.translationX = -500f
                        tvTeks1.alpha = 0f
                        tvTeks1.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(800)
                            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                            .start()

                        tvTeks2.visibility = View.VISIBLE
                        tvTeks2.translationX = -500f
                        tvTeks2.alpha = 0f
                        tvTeks2.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(800)
                            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                            .start()

                        // Animasi tombol fade-in
                        btnToLogin.visibility = View.VISIBLE
                        btnToLogin.alpha = 0f
                        btnToLogin.animate()
                            .alpha(1f)
                            .setDuration(600)
                            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                            .start()

                        btnToRegister.visibility = View.VISIBLE
                        btnToRegister.alpha = 0f
                        btnToRegister.animate()
                            .alpha(1f)
                            .setDuration(600)
                            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                            .start()
                    }.start()
            }.start()
    }

    // Tombol "Continue" untuk swipe terakhir
    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}
