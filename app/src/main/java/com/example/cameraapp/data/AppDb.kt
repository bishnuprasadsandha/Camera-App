package com.example.cameraapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Session::class, Photo::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun photoDao(): PhotoDao
}