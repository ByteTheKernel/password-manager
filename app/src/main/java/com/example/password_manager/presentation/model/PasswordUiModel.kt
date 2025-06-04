package com.example.password_manager.presentation.model

data class PasswordUiModel(
    val id: Long = 0,
    val title: String = "",
    val login: String = "",
    val password: String = "",
    val url: String? = null,
    val description: String? = null,
    val iconUrl: String = ""
)