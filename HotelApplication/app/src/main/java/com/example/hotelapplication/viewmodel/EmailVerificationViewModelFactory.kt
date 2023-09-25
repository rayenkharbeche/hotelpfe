package com.example.hotelapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.ClientRepository

class EmailVerificationViewModelFactory(private val clientRepository: ClientRepository, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailVerificationViewModel::class.java)) {
            return EmailVerificationViewModel(clientRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
