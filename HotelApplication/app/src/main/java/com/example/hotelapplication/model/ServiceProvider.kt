package com.example.hotelapplication.model

import android.os.Parcelable
import androidx.room.Entity
//import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity
//@Parcelize
data class ServiceProvider(val id: String = UUID.randomUUID().toString(),
                           val name: String,
                           val email: String,
                           val password: String,
                           val serviceId: String,
                           val adminId: String)//: Parcelable
