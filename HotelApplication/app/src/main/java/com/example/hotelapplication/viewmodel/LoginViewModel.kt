package com.example.hotelapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.data.*
import com.example.hotelapplication.model.Client

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class LoginViewModel(val clientRepository: ClientRepository, val application: Application, val context: Context) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    val client: MutableLiveData<Client> = MutableLiveData()

    val emailToken: MutableLiveData<String> = MutableLiveData()




    val signUpResult: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()
    val signInResult: MutableLiveData<ApiResponse<String>> = MutableLiveData()
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage

    fun getIsLoading(): LiveData<Boolean> = isLoading

    fun signUp(loginBody: LoginBody) {
        clientRepository.signUp(loginBody, object : Callback<ApiResponse<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.isSuccessful) {
                    signUpResult.value = response.body()
                    Log.d(TAG, "Sign up response: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Sign up failed. Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e(TAG, "Sign up failed", t)
            }
        })
    }


    companion object {
        const val TAG = "LoginViewModel"
    }

}
