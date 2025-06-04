package com.example.password_manager.utils

import androidx.core.net.toUri

fun getFaviconUrl(url: String?): String {
    val domain = try {
        when {
            url.isNullOrBlank() -> ""
            url.startsWith("http") -> url.toUri().host ?: ""
            else -> {
                "https://$url".toUri().host ?: url
            }
        }
    } catch (e: Exception) {
        ""
    }
    return if (domain.isNotBlank()) {
        "https://www.google.com/s2/favicons?domain=$domain&sz=64"
    } else {
        ""
    }
}
