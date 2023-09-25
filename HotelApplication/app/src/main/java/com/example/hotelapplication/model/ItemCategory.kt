package com.example.hotelapplication.model

import android.os.Parcelable
import androidx.room.Entity
//import kotlinx.android.parcel.Parcelize;
import java.util.*


@Entity
//@Parcelize
data class ItemCategory(val id: String= UUID.randomUUID().toString(),
                        val createdAt: Date?,
                        val updatedAt: Date?,
                        val name: String,
                        val serviceId: String,
                        val items: List<Item>?)//: Parcelable
{


    // Constructeur secondaire pour initialiser datai avec des valeurs spécifiques
    constructor(datai: ItemCategory) : this(
        datai.id,
        datai.createdAt,
        datai.updatedAt,
        datai.name,
        datai.serviceId,
        datai.items
    )
}
