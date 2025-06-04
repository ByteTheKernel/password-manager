package com.example.password_manager.domain.model

data class Password(
    val id: Long,
    val title: String,
    val login: String,
    val password: String,
    val url: String?,
    val description: String?
)