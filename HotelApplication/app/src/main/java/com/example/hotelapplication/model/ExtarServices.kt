package com.example.hotelapplication.model



import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

import java.util.*


@Parcelize
data class ExtarServices(
    @PrimaryKey
    val id: String? = UUID.randomUUID().toString(),
    val name: String?,
    val phone: String?,
    val photo: String?,
    val timeIn: String?,
    val timeOut: String?,
    val status: Boolean?,
    val adminId: String?,
    val description: String?
) : Parcelable