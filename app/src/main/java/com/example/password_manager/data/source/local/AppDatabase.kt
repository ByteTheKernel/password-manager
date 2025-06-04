package com.example.password_manager.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.password_manager.data.model.PasswordEntity
import com.example.password_manager.data.source.local.PasswordDao

@Database(
    entities = [PasswordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}
