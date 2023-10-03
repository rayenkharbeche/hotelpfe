package com.example.hotelapplication.Token

import com.example.hotelapplication.data.LoginResponse
import com.example.hotelapplication.model.Admin
import com.example.hotelapplication.model.Client

object AuthAdmin {
    var admin: Admin? = null
    var reslogin:LoginResponse? = null
    private var instance: AuthAdmin? = null

    fun getInstance(): AuthAdmin {
        if (instance == null) {
            instance = AuthAdmin
        }
        return instance!!
    }


}