package com.example.hotelapplication.Repository


import com.example.hotelapplication.Api.RetrofitService
import com.example.hotelapplication.data.ServiceResponse
import com.example.hotelapplication.model.ExtarServices
import retrofit2.Response


class ServicesRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getAllServices(): Response<List<ExtarServices>> {
        return retrofitService.getAllServices()
    }
}






