package com.example.password_manager.presentation.model

import androidx.core.net.toUri
import com.example.password_manager.domain.model.Password
import com.example.password_manager.utils.getFaviconUrl

fun Password.toUiModel(): PasswordUiModel {
   val iconUrl = getFaviconUrl(url)

    return PasswordUiModel(
        id = id,
        title = title,
        login = login,
        password = password,
        url = url,
        description = description,
        iconUrl = iconUrl
    )
}

fun PasswordUiModel.toDomain(): Password {
    return Password(
        id = id,
        title = title,
        login = login,
        password = password,
        url = url,
        description = description
    )
}

