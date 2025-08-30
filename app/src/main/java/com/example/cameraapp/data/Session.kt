package com.example.cameraapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey val sessionId: String,    // UUID string
    val name: String,
    val age: Int,
    val createdAt: Long
)