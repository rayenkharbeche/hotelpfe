package com.example.hotelapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Place (
    @PrimaryKey
    var id:String ,
    var type:String ,
    )