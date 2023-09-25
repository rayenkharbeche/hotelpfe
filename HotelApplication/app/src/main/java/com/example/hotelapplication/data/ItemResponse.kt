package com.example.hotelapplication.data

import android.os.Parcelable
import com.example.hotelapplication.model.Command
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
class ItemResponse(val id: String,
                   val name: String,
                   val description: String,
                   val photo: String,
                   val price: Double,
                   var isFavorite: Boolean = false
): Parcelable {
    fun getPriceAsString(): String {
        return "$price DT"
    }

}