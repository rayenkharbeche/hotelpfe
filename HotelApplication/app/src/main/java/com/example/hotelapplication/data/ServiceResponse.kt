package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ServiceResponse(
val id : String,
val name: String,
val phone: String,
val photo: String,
val description: String,

) : Parcelable {
}
