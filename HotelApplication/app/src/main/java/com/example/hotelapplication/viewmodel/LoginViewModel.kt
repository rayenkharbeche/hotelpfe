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
    //val navigateToNextFragment = MutableLiveData<Boolean>()

  //  private val isUniqueEmail: MutableLiveData<Boolean> = MutableLiveData(false)

   // private var userLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val emailToken: MutableLiveData<String> = MutableLiveData()
   //fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    //fun getClient(): LiveData<Client> = client
    //fun getUserLoggedIn(): LiveData<Boolean> = userLoggedIn
    //private lateinit var viewModel: LoginViewModel




    val signUpResult: MutableLiveData<ApiResponse<Boolean>> = MutableLiveData()
    val signInResult: MutableLiveData<ApiResponse<String>> = MutableLiveData()
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage

    fun getIsLoading(): LiveData<Boolean> = isLoading

    //val errorMessagee = MutableLiveData<String?>()
    //private var dataa: MutableLiveData<LoginResponse?> = MutableLiveData()
    //fun getAdmin(): MutableLiveData<LoginResponse?> = dataa
    //fun getUser(): LiveData<Client> = client

    /*  fun loginAdmin(body: LoginBody) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = consumer.signIn(body.email, body.emailToken)
                isLoading.value = false

                if (response.success) {
                    val loginResponse = response.data
                    val admin = loginResponse?.data

                    Log.d("LoginViewModel", "Admin data: $admin")

                    admin?.let {
                        AuthToken.getInstance(getApplication()).token = it
                    }

                    Log.d("connected", "connected")
                } else {
                    errorMessage.value = response.error
                    Log.d("error", "error")
                }
            } catch (e: Exception) {
                errorMessage.value = "Error occurred during login"
                Log.e("LoginViewModel", "Error: ${e.message}")
            }
        }
    }*/
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


    /*  fun signIn(email: String, emailToken: String) {
        val loginToken = logintoken(email, emailToken)
        clientRepository.signIn(loginToken, object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    signInResult.value = response.body()
                    Log.d(
                        RegisterActivityViewModel.TAG,
                        "Sign in successful. Response: ${response.body()}"
                    )
                } else {
                    Log.e(
                        RegisterActivityViewModel.TAG,
                        "Sign in failed with response code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.e(RegisterActivityViewModel.TAG, "Sign in failed", t)
            }
        })
    }*/
    companion object {
        const val TAG = "LoginViewModel"
    }

}
