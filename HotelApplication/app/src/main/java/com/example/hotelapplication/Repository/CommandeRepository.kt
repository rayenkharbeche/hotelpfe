package com.example.hotelapplication.Repository


import com.example.hotelapplication.Api.APIConsumer
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Api.SimplifiedMessage
import com.example.hotelapplication.data.RequestStatus
import com.example.hotelapplication.model.Command

import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CommandeRepository (val consumer : APIConsumer){

        suspend fun createCommand(body: Command) = flow {
            emit(RequestStatus.Waiting)
            try {
                val response = consumer.createFacture(body)

                if (response.isSuccessful) {
                    emit(RequestStatus.Success(response.body()!!))
                } else {
                    emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
                }
            } catch (e: Exception) {
                emit(RequestStatus.Error(hashMapOf("error" to (e.message ?: "Unknown error"))))
            }

        }
}

