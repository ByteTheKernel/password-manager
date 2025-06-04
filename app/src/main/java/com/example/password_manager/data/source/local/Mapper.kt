package com.example.password_manager.data.source.local

import com.example.password_manager.data.model.PasswordEntity
import com.example.password_manager.data.model.PasswordPreviewEntity
import com.example.password_manager.domain.model.Password
import com.example.password_manager.domain.model.PasswordPreview
import com.example.password_manager.presentation.passwords.model.PasswordListItem

fun PasswordEntity.toDomain(decryptedPassword: String) = Password(
    id = id,
    title = title,
    login = login,
    password = decryptedPassword,
    url = url,
    description = description
)

fun PasswordPreviewEntity.toDomain() = PasswordPreview(
    id = id,
    title = title,
    url = url
)