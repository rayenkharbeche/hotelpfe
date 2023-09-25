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

    fun logout() {

        admin = null

    }
    fun getadmintId(): String? {
        return admin?.id
    }
   /* fun getadminPhoto(): String? {
        return admin?.photo

    }*/
    fun getAdminName(): String? {
        return admin?.name

    }

    fun setConnectedClient(adminInstance: Admin) {
        admin = adminInstance
    }


    fun getConnectedadmin():Admin? {
        return admin
    }

    private var adminEmail: String? = null

    fun setAdminEmail(email: String) {
        adminEmail = email
    }

    fun getAdminEmail(): String? {
        return adminEmail
    }

}