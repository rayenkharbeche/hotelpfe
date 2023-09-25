package com.example.hotelapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Repository.ItemCategorieRepository

class ItemCategorieViewModelFactory constructor(private val repository: ItemCategorieRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ItemCategorieViewModel::class.java)) {
            ItemCategorieViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}