package com.example.facesecure.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.facesecure.data.local.Converters

@Entity(tableName = "user")
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String,
    val faceEmbedding: List<Float>? = null
)