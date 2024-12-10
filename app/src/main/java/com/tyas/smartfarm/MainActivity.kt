package com.tyas.smartfarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tyas.smartfarm.databinding.ActivityMainBinding

@Suppress("UNUSED_EXPRESSION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Set startDestination secara dinamis
        navController.setGraph(R.navigation.nav_graph, null)

        // Listener untuk Bottom Navigation
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
                    R.id.navigation_lay_weather, true)
                R.id.plantFragment -> binding.bottomNavigation.setItemSelected(
                    R.id.navigation_lay_plant, true)
                R.id.profileFragment -> binding.bottomNavigation.setItemSelected(
                    R.id.navigation_lay_profile, true)
            }

            // Sembunyikan BottomNavigation di halaman tertentu
            when (destination.id) {
                R.id.settingsFragment,
                R.id.addPlantFragment,
                R.id.splashFragment,
                R.id.onBoardFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.plantCareFragment -> controlBottomNavigationVisibility(false)
                else -> controlBottomNavigationVisibility(true)
            }
        }

        // Buat Notification Channel untuk notifikasi pengingat
        createNotificationChannel()
    }

    // Fungsi untuk mengontrol visibilitas Bottom Navigation
    private fun controlBottomNavigationVisibility(isVisible: Boolean) {
        binding.bottomNavigation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    // Fungsi untuk membuat Notification Channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Pengingat Tanaman"
            val channelDescription = "Kanal untuk pengingat merawat tanaman"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("PLANT_REMINDER_CHANNEL", channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
