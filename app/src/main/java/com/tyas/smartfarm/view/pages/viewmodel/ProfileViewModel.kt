package com.tyas.smartfarm.view.pages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tyas.smartfarm.model.db.AppDatabase
import com.tyas.smartfarm.model.db.User
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()
    val user: LiveData<User> = userDao.getUser()

    fun saveUser(user: User) {
        viewModelScope.launch {
            userDao.insertOrUpdateUser(user)
        }
    }
}