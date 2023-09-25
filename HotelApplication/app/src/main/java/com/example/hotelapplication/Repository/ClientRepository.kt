package com.example.hotelapplication.Repository


import android.util.Log
import com.example.hotelapplication.Api.APIConsumer
import com.example.hotelapplication.Api.SimplifiedMessage
import com.example.hotelapplication.data.*
import com.example.hotelapplication.model.Client
import com.example.hotelapplication.model.Command
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientRepository(val consumer : APIConsumer) {

    /*fun registerUser (body: RegisterBody) = flow {
        emit(RequestStatus.Waiting)
        try {
            val response = consumer.registerUser(body)

            if (response.isSuccessful) {
                emit ((RequestStatus.Success(response.body()!!)))

            } else {

                emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
            }
        } catch (e: Exception) {
            //emit(RequestStatus.Error(hashMapOf("error" to ( "This mail is already in use"))))
            emit(RequestStatus.Error(hashMapOf("error" to (e.message ?: "Unknown error"))))

        }
    }

    fun LoginUser (body: LoginBody) = flow {
        emit(RequestStatus.Waiting)
        try {
            val response = consumer.loginUser(body)
            if (response.isSuccessful) {
                emit ((RequestStatus.Success(response.body()!!)))


            } else {
                emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
            }
        } catch (e: Exception) {
            emit(RequestStatus.Error(hashMapOf("error" to (e.message ?: "Unknown error"))))

        }
    }
*/

    suspend fun updateClient(id: String, updateclientbody: UpdateClientBody): Flow<RequestStatus<Client>> = flow {
        emit(RequestStatus.Waiting)
        try {
            val response = consumer.updateClientt(id, updateclientbody)
            if (response.isSuccessful) {
                emit(RequestStatus.Success(response.body()!!))
            } else {
                emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
            }
        } catch (e: Exception) {
            emit(RequestStatus.Error(hashMapOf("error" to (e.message ?: "Unknown error"))))
        }
    }

    suspend fun getAllClient(): Response<List<Client>> {
        return consumer.getAllClient()
    }

    fun signUp(email: LoginBody, callback: Callback<ApiResponse<Boolean>>) {
        val call = consumer.signUp(email)
        call.enqueue(callback)
    }

    fun signIn(email: String, emailToken: String, callback: Callback<ApiResponse<String>>) {
        val loginToken = logintoken(email, emailToken) // Créez une instance de l'objet LoginToken
        val call = consumer.signIn(loginToken)
        Log.d("SignInRequest", "Request URL: ${call.request().url}")
        call.enqueue(callback)
    }


    /*fun loginAdmin(body : LoginBody)  = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.loginAdmin(body)
        if(response.isSuccessful){
            emit((RequestStatus.Success(response.body()!!)))
        }else{
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

*/
    fun validateAPIToken(authToken: String): Call<TokenResponse> {
        return consumer.validateAPIToken(authToken)
    }


    /*suspend fun getClientById(id: String): Client? {
        try {
            val response = consumer.getClientById(id)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }*/

    suspend fun getCommandHistoryByQuery(clientId: String): Response<List<Command>> {
        return consumer.getCommandHistoryByQuery(clientId)
    }
}




