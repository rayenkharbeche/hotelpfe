package com.example.hotelapplication.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.Repository.ItemCategorieRepository
import com.example.hotelapplication.data.ItemCategoryResponse
import com.example.hotelapplication.model.ItemCategory
import kotlinx.coroutines.launch
import retrofit2.Response

class ItemCategorieViewModel (private val repository: ItemCategorieRepository) : ViewModel() {
    val serviceList = MutableLiveData<List<ItemCategory>>()
    val serviceResponseListLiveData = MutableLiveData<List<ItemCategoryResponse>>()
    val errorMessage = MutableLiveData<String?>()
    private val _ItemC = MutableLiveData<ItemCategory>()

    fun getItemCategorie(serviceId: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<ItemCategory>> = repository.getItemCategorie(serviceId)
                if (response.isSuccessful) {
                    val extarItemcList: List<ItemCategory>? = response.body()

                    val ItemcResponseList: List<ItemCategoryResponse> = extarItemcList?.map { Itemc ->
                            ItemCategoryResponse(Itemc.id ?: "",Itemc.name ?: "")
                        } ?: emptyList()

                    // Poster la liste
                    serviceResponseListLiveData.postValue(ItemcResponseList)
                    Log.d(ContentValues.TAG, "Item Categorie list loaded successfully.")
                } else {
                    val errorString = response.errorBody()?.string()
                    errorMessage.postValue(errorString)
                }
            } catch (t: Throwable) {
                errorMessage.postValue(t.message)
                Log.e(ContentValues.TAG, "Failed to load item categorie list: ${t.message}")
            }
        }
    }
}


