package com.example.hotelapplication.viewmodel

import CommandeViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.CommandeRepository

class CommandeViewModelFactory  (private val commandeRepository: CommandeRepository, private val application: Application):
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommandeViewModel::class.java)) {
            return CommandeViewModel(commandeRepository, application) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}