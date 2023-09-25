package com.example.hotelapplication.Repository

import com.example.hotelapplication.Api.APIConsumer


class AuthRepository(val consumer: APIConsumer) {

  /*  fun validateEmailAddress(body: ValidateEmailBody): Flow<RequestStatus<ValidateEmailBody>> = flow {
        emit(RequestStatus.Waiting)
        try {
            val response = consumer.validateEmailAdrress(body)
            if (response.isSuccessful) {
               // emit(RequestStatus.Success(response.body()!!))
            } else {
               // emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()?.byteStream()?.reader()?.readText())))
            }
        } catch (e: Exception) {
          //  emit(RequestStatus.Error(e.message ?: "An error occurred"))
        }
    }


    fun registerAdmin(body : AdminDataItem)  = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerAdmin(body)
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

    fun loginAdmin(body : LoginBody)  = flow {
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
    }*/
}

