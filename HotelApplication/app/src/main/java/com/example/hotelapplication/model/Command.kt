package com.example.hotelapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Command(
    val id: String= UUID.randomUUID().toString(),
    val date: String?= null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val treatementDuration: String?= null,
    val totalPrice: Float?= null,
    val commandStatus: CommandStatus?= null,
    val itemIds: List<String>?= null,
    val providerId: String?= null,
    val clientId: String?= null,
    val items: List<Item>?= null): Parcelable{}