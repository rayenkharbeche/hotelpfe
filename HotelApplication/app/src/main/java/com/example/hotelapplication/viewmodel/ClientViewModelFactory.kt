package com.example.hotelapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.ClientRepository

class ClientViewModelFactory  constructor(private val repository: ClientRepository ,  private val application: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            ClientViewModel(repository,application) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}