package com.example.hotelapplication.Api



import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.hotelapplication.data.*
import com.example.hotelapplication.model.Client
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.model.ExtarServices
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



interface APIConsumer {

    @POST("api/client/")
    suspend fun registerUser(@Body body: RegisterBody): Response<RegisterResponse>

    @POST("api/client/")
    suspend fun loginUser (@Body body: LoginBody): Response<RegisterResponse>

    @PUT("api/client/update/{id}")
    suspend fun updateClient(@Path("id") id: String, @Body client: Client): Response<Client>

    @POST("api/command/postC")
    suspend fun createFacture(@Body facture: Command): Response<Command>

    @Headers("Content-Type: application/json")
    @GET("api/service/{id}")
    suspend fun getServiceListe(
        @Header("Authorization") auth: String,
        @Header("Role") role: String,
        @Path("id") id: String
    ): Response<ArrayList<ExtarServices>>


    @GET("api/client/listC")
    suspend fun getAllClient(): Response<List<Client>>

    @POST("/api/client/register")
    fun signIn(@Body loginToken: logintoken): Call<ApiResponse<String>>


    @POST("/api/client/login")
    fun signUp(@Body email: LoginBody): Call<ApiResponse<Boolean>>



    @POST("api/account/login")
    suspend fun loginAdmin(@Body body : LoginBody):Response<LoginResponse>

    @GET("/api/client/validate-token/{authToken}")
    fun validateAPIToken(@Path("authToken") authToken: String):  Call<TokenResponse>


    @PUT("api/client/update/{id}")
    suspend fun updateClientt(@Path("id") id: String, @Body updateclient: UpdateClientBody): Response<Client>


    @GET("clientid/{id}")
    suspend fun getClientById(@Path("id") id: String): Response<Client>

    @GET("api/history/getCommandHistoryByQuery/{clientId}")
    suspend fun getCommandHistoryByQuery(@Path("clientId") clientId: String): Response<List<Command>>


}



