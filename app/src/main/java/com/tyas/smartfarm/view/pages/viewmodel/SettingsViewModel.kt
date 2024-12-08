package com.tyas.smartfarm.view.pages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.util.DataStoreManager
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    val notificationsEnabled = dataStoreManager.getNotificationsEnabled().asLiveData()
    val selectedLanguage = dataStoreManager.getLanguage().asLiveData()

    fun updateNotificationsEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setNotificationsEnabled(isEnabled)
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            dataStoreManager.setLanguage(language)
        }
    }

    // Factory untuk membuat instance ViewModel
    class Factory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(dataStoreManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
