package com.example.hotelapplication.Converter

import androidx.room.TypeConverter
import java.util.*

class Converter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}