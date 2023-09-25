package com.example.hotelapplication.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.Repository.ItemRepository
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.model.Item

import kotlinx.coroutines.launch
import retrofit2.Response

class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    val serviceList = MutableLiveData<List<Item>>()
    val ItemResponseListLiveData = MutableLiveData<List<ItemResponse>>()
    val errorMessage = MutableLiveData<String?>()


    fun getItem(itemCategoryId: String) {
        viewModelScope.launch {
            try {
                val response: Response<List<Item>> = repository.getItem(itemCategoryId)
                if (response.isSuccessful) {

                    val extarItemcList: List<Item>? = response.body()
                    val ItemResponseList: List<ItemResponse> = extarItemcList?.map { Itemc ->
                        Log.d(ContentValues.TAG, "Price before conversion: ${Itemc.price}")
                        val convertedPrice = Itemc.price.toString().toDouble()
                        Log.d(ContentValues.TAG, "Price after conversion: $convertedPrice")
                            ItemResponse(Itemc.id ,Itemc.name ,Itemc.description,Itemc.photo ,convertedPrice)
                        } ?: emptyList()

                    // Poster la liste
                    ItemResponseListLiveData.postValue(ItemResponseList)
                    Log.d(ContentValues.TAG, "Item  list loaded successfully.")
                } else {
                    val errorString = response.errorBody()?.string()
                    errorMessage.postValue(errorString)
                }
            } catch (t: Throwable) {
                errorMessage.postValue(t.message)
                Log.e(ContentValues.TAG, "Failed to load item list: ${t.message}")
            }
        }
    }
}


