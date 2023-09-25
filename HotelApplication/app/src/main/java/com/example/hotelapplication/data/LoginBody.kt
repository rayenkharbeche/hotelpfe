package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginBody(val email: String, ): Parcelable {

}//val password:String
