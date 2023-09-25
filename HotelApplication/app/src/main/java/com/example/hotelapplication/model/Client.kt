package com.example.hotelapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


import java.util.*


@Parcelize
data class Client(

    var id :String?= UUID.randomUUID().toString(),
    var name  : String?= null,
    var email :String?= null,
    var phone : String?= null,
    var check_in :  String?= null,
    var check_out : String?= null,
    var photo   :  String?= null,
    var credit:String?= null,
    var adminId:String?= null,
    val history: MutableList<Command>
    ):Parcelable {
    fun getIdValue(): String? {
        return id
    }

    fun getNameValue(): String? {
        return name
    }

    fun getEmailValue(): String? {
        return email
    }


    fun getPhoneValue(): String? {
        return phone
    }

    fun getCheckInValue(): String? {
        return check_in
    }

    fun getCheckOutValue(): String? {
        return check_out
    }

    fun getPhotoValue(): String? {
        return photo
    }

    fun getCreditValue(): String? {
        return credit
    }

    fun getAdminIdValue(): String? {
        return adminId
    }
}