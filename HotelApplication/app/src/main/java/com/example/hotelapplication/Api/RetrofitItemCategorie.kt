package com.example.hotelapplication.Api

import com.example.hotelapplication.model.ItemCategory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitItemCategorie {


    @GET("api/category/list/{serviceId}")
    suspend fun getItemCategorie( @Path("serviceId") serviceId: String): Response<List<ItemCategory>>



    companion object {
        private const val BASE_URL = "http://192.168.1.34:3000/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getInstance(): RetrofitItemCategorie {
            return retrofit.create(RetrofitItemCategorie::class.java)
        }
    }
}
