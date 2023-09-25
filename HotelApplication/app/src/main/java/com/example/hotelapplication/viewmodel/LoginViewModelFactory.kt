package com.example.hotelapplication.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.ClientRepository



class LoginViewModelFactory (private val clientRepository: ClientRepository, private val application: Application,val context: Context):
    ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return  LoginViewModel(clientRepository,application,context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}
