package com.example.facesecure.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListToString(list: List<Float>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(data: String?): List<Float>? {
        return data?.split(",")?.mapNotNull { it.toFloatOrNull() }
    }
}