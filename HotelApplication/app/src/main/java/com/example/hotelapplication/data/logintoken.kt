package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class logintoken(val email: String,
                         val emailToken: String): Parcelable {
}