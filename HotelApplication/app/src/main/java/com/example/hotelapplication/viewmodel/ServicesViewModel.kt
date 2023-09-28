package com.example.hotelapplication.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.hotelapplication.Repository.ServicesRepository
import com.example.hotelapplication.data.ServiceResponse
import com.example.hotelapplication.model.ExtarServices
import kotlinx.coroutines.launch
import retrofit2.Response

class ServicesViewModel(private val repository: ServicesRepository) : ViewModel() {
    val serviceResponseListLiveData = MutableLiveData<List<ServiceResponse>>()
    val errorMessage = MutableLiveData<String?>()

    fun getAllServices() {
        viewModelScope.launch {
            try {
                val response: Response<List<ExtarServices>> = repository.getAllServices()
                if (response.isSuccessful) {
                    val extarServicesList: List<ExtarServices>? = response.body()

                    val serviceResponseList: List<ServiceResponse> = extarServicesList?.map { extarService ->
                        ServiceResponse(extarService.id ?: "",extarService.name ?: "", extarService.phone ?: "", extarService.photo ?: "", extarService.description ?: "")
                    } ?: emptyList()

                    // Poster la liste de ServiceResponse
                    serviceResponseListLiveData.postValue(serviceResponseList)
                    Log.d(TAG, "Service list loaded successfully.")
                } else {
                    val errorString = response.errorBody()?.string()
                    errorMessage.postValue(errorString)
                }
            } catch (t: Throwable) {
                errorMessage.postValue(t.message)
                Log.e(TAG, "Failed to load service list: ${t.message}")
            }
        }
    }
}