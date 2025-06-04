package com.example.password_manager.presentation.passwords.model

import android.util.Log
import androidx.core.net.toUri
import com.example.password_manager.domain.model.PasswordPreview
import com.example.password_manager.utils.getFaviconUrl

fun PasswordPreview.toListItem(): PasswordListItem {

    val iconUrl = getFaviconUrl(url)

    return PasswordListItem(id, title, iconUrl)
}
