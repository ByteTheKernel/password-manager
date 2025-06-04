package com.example.password_manager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val login: String,
    val encryptedPassword: String,
    val url: String?,
    val description: String?
)