package com.example.hotelapplication.data



import android.os.Parcelable
import com.example.hotelapplication.model.Item
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
class CommandResponse (

    val id: String = UUID.randomUUID().toString(),
    val totalPrice: Double,
    val date: String,
    val treatementDuration: String,
    val clientId: String?,
    val items: List<Item>,

    ): Parcelable {}