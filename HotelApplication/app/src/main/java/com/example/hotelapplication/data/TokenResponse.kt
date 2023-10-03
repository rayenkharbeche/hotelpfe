package com.example.hotelapplication.data

import android.os.Parcelable
import com.example.hotelapplication.model.Client
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenResponse(var client: Client, var token: String) : Parcelable {

}

