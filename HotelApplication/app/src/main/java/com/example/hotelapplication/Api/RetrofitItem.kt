package com.example.hotelapplication.Api


import com.example.hotelapplication.model.Item
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitItem {


    @GET("api/item/listes/{serviceId}")
    suspend fun getItem( @Path("serviceId") serviceId: String): Response<List<Item>>



    companion object {
        private const val BASE_URL = "http://192.168.1.23:3000/"

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getInstance(): RetrofitItem {
            return retrofit.create(RetrofitItem::class.java)
        }
    }
}
