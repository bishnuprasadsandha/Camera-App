package com.example.cameraapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photos WHERE sessionId = :sessionId ORDER BY takenAt DESC")
    fun photosForSession(sessionId: String): Flow<List<Photo>>
}