package com.example.hotelapplication.model

import android.os.Parcelable
import androidx.room.*

import java.util.*


data class Service(
  @PrimaryKey
  val id:String = UUID.randomUUID().toString(),
  val Libelle:String,
  val Description:String,
  val Etat:String,
  val Prix: Double,
  val ImageUrl:String,
  val Typeserv:String,
  val Note:Float ,


)





