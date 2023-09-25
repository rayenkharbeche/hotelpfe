package com.example.hotelapplication.Api


import com.example.hotelapplication.model.ExtarServices
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    @GET("api/service/listSer")
    suspend fun getAllServices(): Response<List<ExtarServices>>



    companion object {
        private const val BASE_URL = "http://192.168.1.34:3000/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getInstance(): RetrofitService {
            return retrofit.create(RetrofitService::class.java)
        }
    }
}
