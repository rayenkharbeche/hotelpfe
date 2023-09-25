package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class UpdateClientBody ( var email :String?,var phone : String?,var name  : String?): Parcelable {
}