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
    val navigateToNextFragment = MutableLiveData<Boolean>()
    private var userLoggedIn: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }


    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage

    fun getIsLoading(): LiveData<Boolean> = isLoading
    val errorMessagee = MutableLiveData<String?>()


    fun getUser(): LiveData<Client> = client


   /* suspend fun activateAccount(email: String, emailToken: String) {
        val response= clientRepository.signUp(email, emailToken)

        if (response.isSuccessful) {
            val apiResponse: ApiResponse<String>? = response.body()
            if (apiResponse != null) {
                // Traitement de la réponse réussie
                if (apiResponse.success) {
                    // Le compte a été activé avec succès
                    val message = apiResponse.data // Message de succès
                    Log.d("activateAccount", "Account activation successful: $message")
                    // Effectuer les actions appropriées, par exemple, afficher un message à l'utilisateur
                } else {
                    // La requête a réussi, mais l'activation du compte a échoué
                    val errorMessage = apiResponse.message // Message d'erreur
                    Log.e("activateAccount", "Account activation failed: $errorMessage")
                    // Effectuer les actions appropriées, par exemple, afficher un message à l'utilisateur indiquant que l'activation du compte a échoué
                }
            } else {
                // La réponse est nulle, gérer cette situation d'erreur
                Log.e("activateAccount", "Null response received")
            }
        } else {
            // Gestion de l'échec de la requête
            val errorBody: ResponseBody? = response.errorBody()
            val errorCode: Int = response.code()
            Log.e("activateAccount", "Request failed with error code: $errorCode")
            // Effectuer les actions appropriées pour gérer l'échec de la requête, par exemple, afficher un message d'erreur à l'utilisateur
        }
    }
*/

}