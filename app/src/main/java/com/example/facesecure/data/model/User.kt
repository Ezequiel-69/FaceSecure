package com.example.facesecure.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val nombre: String,
    val email: String,
    val password: String
)
