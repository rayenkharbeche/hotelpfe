package com.example.hotelapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Token.AuthClient
import com.example.hotelapplication.Token.AuthClient.setConnectedClient
import com.example.hotelapplication.data.ApiResponse
import com.example.hotelapplication.data.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivityViewModel(
    application: Application,
    private val clientRepository: ClientRepository
) : AndroidViewModel(application) {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    val signInResponse: MutableLiveData<ApiResponse<String>?> = MutableLiveData()
    private val isUniqueEmail: MutableLiveData<Boolean> = MutableLiveData(false)


    fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getSignInResponse(): LiveData<ApiResponse<String>?> = signInResponse


    //var _signInResponseLiveData: MutableLiveData<ApiResponse<String>?> = MutableLiveData()
    val signInResponseLiveData = MutableLiveData<ApiResponse<String>?>()


    private val _tokenResponse: MutableLiveData<TokenResponse?> = MutableLiveData()
    val tokenResponse: LiveData<TokenResponse?> get() = _tokenResponse

    val clientName: MutableLiveData<String> = MutableLiveData("")
    val clientEmail: MutableLiveData<String> = MutableLiveData("")
    val clientPhone: MutableLiveData<String> = MutableLiveData("")
    val clientCredit: MutableLiveData<String> = MutableLiveData("")
    val clientPhoto: MutableLiveData<String?> = MutableLiveData(null)
    val _clientIdLiveData: MutableLiveData<String?> = MutableLiveData()
    val clientIdLiveData: MutableLiveData<String?> = _clientIdLiveData

    val _clientNameLiveData: MutableLiveData<String?> = MutableLiveData()
    var clientNameLiveData: MutableLiveData<String?> = _clientNameLiveData

    val _clientEmailLiveData: MutableLiveData<String?> = MutableLiveData()
    val clientEmailLiveData: MutableLiveData<String?> = _clientEmailLiveData

    val _clientPhotoLiveData: MutableLiveData<String?> = MutableLiveData()
    val clientPhotoLiveData: MutableLiveData<String?> = _clientPhotoLiveData

    companion object {
        const val TAG = "RegisterViewModel"
        var clientId: String? = null
        var clientnamee: String? = null
        var clientemaill: String? = null
        var clientphotoo: String? = null
    }

    fun signIn(email: String, emailToken: String): LiveData<String?> {
        val authTokenLiveData = MutableLiveData<String?>()
        isLoading.value = true

        clientRepository.signIn(email, emailToken, object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val signInResponse = response.body()
                //    Log.d(TAG, " response: $signInResponse")
                    if (signInResponse != null) {
                        signInResponseLiveData.value = signInResponse

                        if (signInResponse.success && !signInResponse.data.isNullOrEmpty()) {
                            val authToken = signInResponse.data!!
                            authTokenLiveData.value = authToken

                            validateAPIToken(authToken)

                        } else {
                            Log.d(TAG, "Sign in failed: Invalid response")
                        }
                    } else {
                        Log.d(TAG, "Sign in failed: Response body is null")
                    }
                } else {
                    Log.d(TAG, "Sign in failed with response code: ${response.code()}")
                    if (response.errorBody() != null) {
                        try {
                            val errorResponse = response.errorBody()!!.string()
                            Log.d(TAG, "Sign in failed: Error response body: $errorResponse")
                        } catch (e: Exception) {
                            Log.d(TAG, "Error parsing error response body", e)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                isLoading.value = false
                Log.e(TAG, "Sign in request failed", t)
            }
        })

        return authTokenLiveData
    }


    fun validateAPIToken(authToken: String): LiveData<TokenResponse?> {
        val tokenResponseLiveData = MutableLiveData<TokenResponse?>()


        clientRepository.validateAPIToken(authToken).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        tokenResponseLiveData.value = tokenResponse

                        setConnectedClient(tokenResponse.client)
                        clientnamee = tokenResponse.client.name
                        clientNameLiveData.value = tokenResponse.client.name
                        clientemaill = tokenResponse.client.email
                        clientEmailLiveData.value = tokenResponse.client.email
                        clientphotoo = tokenResponse.client.photo
                        clientPhotoLiveData.value =  clientphotoo
                        Log.d(TAG, "Token validation successful. clientNameLiveData.value: ${clientNameLiveData.value}")
                        Log.d(TAG, "Token validation successful. clientEmailLiveData.valuel: ${clientEmailLiveData.value}")
                       // Log.d(TAG, "Token validation successful. Name: ${tokenResponse.client.name}")
                        //Log.d(TAG, "Token validation successful. Phone: ${tokenResponse.client.phone}")
                      //  Log.d(TAG, "Token validation successful. Credit: ${tokenResponse.client.credit}")
                        clientId =  tokenResponse.client.id
                        clientIdLiveData.value = tokenResponse.client.id
                        setClientId(clientId)
                       // handleLoginSuccess(tokenResponse.client)
                        tokenResponse.client.name
                        clientNameLiveData.value = tokenResponse.client.name
                        setClientName(clientnamee)

                        tokenResponse.client.email
                        clientEmailLiveData.value = tokenResponse.client.email
                        setClientName(clientemaill)

                        clientphotoo = tokenResponse.client.photo
                        clientPhotoLiveData.value =  clientphotoo
                        setClientName(clientphotoo)

                        Log.d(TAG, "Token validation successful.clientIdLiveData: ${clientIdLiveData.value}")
                    //    Log.d(TAG, "Token validation successful.clientNameLiveData: ${clientNameLiveData.value}")
                    } else {
                        handleErrorMessage("Token response body is null")
                    }
                } else {
                    handleErrorMessage("Failed to validate token: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                handleErrorMessage("An error occurred during token validation", t)
            }
        })

        return tokenResponseLiveData
    }


    private fun handleErrorMessage(message: String, throwable: Throwable? = null) {
        val errorMessage = "Error: $message"
        Log.e(TAG, errorMessage, throwable)
        this.errorMessage.value = errorMessage
    }



    fun getClientId(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientId
        return tokenResponseLiveData
    }
    fun getClientname(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientnamee
        return tokenResponseLiveData
    }
    fun getClientemail(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientemaill
        return tokenResponseLiveData
    }
    fun setClientId(clientId: String?) {
        clientIdLiveData.value = clientId
    }


    fun getClientNamee(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientnamee
        return tokenResponseLiveData
    }
    fun setClientName(clientName: String?) {
        _clientNameLiveData.value = clientName
    }


    fun getClientPhotoo(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientphotoo
        return tokenResponseLiveData
    }
    fun setClientPhotoo(clientPho: String?) {
        _clientNameLiveData.value = clientphotoo
    }
    // Fonctions pour obtenir et définir l'e-mail du client
 /*   fun getClientEmail(): MutableLiveData<String?> {
        val tokenResponseLiveData = MutableLiveData<String?>()
        tokenResponseLiveData.value = clientemail
        return tokenResponseLiveData
    }

    fun setClientEmail(clientEmail: String?) {
        _clientEmailLiveData.value = clientEmail
    }*/

    fun fetchClientNameAndEmail(): Pair<String?, String?>? {
        val connectedClient = AuthClient.getConnectedClient()
        return if (connectedClient != null) {
            Pair(connectedClient.name, connectedClient.email)
        } else {
            null
        }
    }

}