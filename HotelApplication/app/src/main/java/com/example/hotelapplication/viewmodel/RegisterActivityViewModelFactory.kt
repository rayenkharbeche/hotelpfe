package com.example.hotelapplication.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.ClientRepository


class RegisterActivityViewModelFactory(private val application: Application, private val clientRepository: ClientRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(application, clientRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

