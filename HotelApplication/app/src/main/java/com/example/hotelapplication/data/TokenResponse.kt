package com.example.hotelapplication.data

import android.os.Parcelable
import com.example.hotelapplication.model.Client
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenResponse(var client: Client, var token: String) : Parcelable {
    fun getClientObject(): Client {
        return client
    }

    fun getTokenValue(): String {
        return token
    }
}

/*val email: String,
                          val name: String,
                          val phone: String,
                          //val photo: String,
                          var credit:String?,
                          //val token: String*/