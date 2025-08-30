package com.example.cameraapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: Session)

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE sessionId = :id LIMIT 1")
    suspend fun getById(id: String): Session?

    @Query("SELECT * FROM sessions WHERE name LIKE '%' || :q || '%' OR sessionId LIKE '%' || :q || '%'")
    fun search(q: String): Flow<List<Session>>
}