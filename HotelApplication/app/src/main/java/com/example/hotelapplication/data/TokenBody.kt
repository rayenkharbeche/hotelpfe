package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TokenBody(val authToken: String): Parcelable{}
