package com.example.cameraapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val filePath: String,    // absolute path in app-specific dir
    val takenAt: Long
)