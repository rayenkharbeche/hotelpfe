package com.example.hotelapplication.Converter

import androidx.room.TypeConverter
import com.example.hotelapplication.model.Command

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {


    @TypeConverter
    fun fromCommandList(value: List<Command>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Command>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCommandList(value: String): List<Command> {
        val gson = Gson()
        val type = object : TypeToken<List<Command>>() {}.type
        return gson.fromJson(value, type)
    }

}