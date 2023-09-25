package com.example.hotelapplication.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.util.*



@Parcelize
data class Item(val id: String= UUID.randomUUID().toString(),
                val name: String,
                val description: String,
                val photo: String,
                val price: Double,
                val status: Boolean,
                val serviceId: String,
                val itemCategoryId: String,
                val commands: List<Command>?): Parcelable
{
    fun getPriceAsString(): String {
        return "$price DT"
    }

}
