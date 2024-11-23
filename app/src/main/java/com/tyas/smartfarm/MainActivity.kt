package com.tyas.smartfarm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tyas.smartfarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener { menuItemId ->
            when (menuItemId) {
                R.id.navigation_lay_weather -> {
                    if (navController.currentDestination?.id != R.id.weatherFragment) {
                        navController.navigate(R.id.weatherFragment)
                    }
                }
                R.id.navigation_lay_plant -> {
                    if (navController.currentDestination?.id != R.id.plantFragment) {
                        navController.navigate(R.id.plantFragment)
                    }
                }
                R.id.navigation_lay_profile -> {
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.profileFragment)
                    }
                }
                else -> false
            }
            true
        }

        // Listener untuk memperbarui indikator Bottom Navigation
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.weatherFragment -> binding.bottomNavigation.setItemSelected(
                    R.id.navigation_lay_weather,
                    true
                )
                R.id.plantFragment -> binding.bottomNavigation.setItemSelected(
                    R.id.navigation_lay_plant,
                    true
                )
                R.id.profileFragment -> binding.bottomNavigation.setItemSelected(
                    R.id.navigation_lay_profile,
                    true
                )
            }

            // Sembunyikan BottomNavigation di halaman tertentu
            when (destination.id) {
                R.id.splashFragment,
                R.id.onBoardFragment,
                R.id.loginFragment,
                R.id.registerFragment -> controlBottomNavigationVisibility(false)
                else -> controlBottomNavigationVisibility(true)
            }
        }
    }

    // Fungsi untuk mengontrol visibilitas Bottom Navigation
    private fun controlBottomNavigationVisibility(isVisible: Boolean) {
        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
