package com.example.hotelapplication.Api


import android.content.Context
import com.example.hotelapplication.Token.AuthToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object APIService {
    private const val BASE_URL = "http://192.168.1.34:3000/"

    fun getService(): APIConsumer {

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)
    }

    fun getUpdatedService(): APIConsumer {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)
    }


    private fun createHttpClient(context: Context): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val authToken = AuthToken.getInstance(context)
            val token = authToken.token ?: ""

            if (token.isNotBlank()) {
                val requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()

                chain.proceed(requestWithToken)
            } else {
                chain.proceed(originalRequest)
            }
        }

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    private var retrofit: Retrofit? = null
    fun getService(context: Context): APIConsumer {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(APIConsumer::class.java)
    }

}



