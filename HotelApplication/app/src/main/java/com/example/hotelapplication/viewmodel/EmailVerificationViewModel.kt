package com.example.hotelapplication.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.model.Client

class EmailVerificationViewModel @JvmOverloads constructor(val clientRepository: ClientRepository, val application: Application): ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    val client: MutableLiveData<Client> = MutableLiveData()


}