package com.example.password_manager.domain.repository

import androidx.paging.PagingData
import com.example.password_manager.domain.model.Password
import com.example.password_manager.domain.model.PasswordPreview
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    fun getPasswordPreviewsPaging(query: String): Flow<PagingData<PasswordPreview>>
    fun getAllPasswordPreviews(): Flow<List<PasswordPreview>>
    fun searchPasswordPreviews(query: String): Flow<List<PasswordPreview>>
    suspend fun getPasswordById(id: Long): Password?
    suspend fun addPassword(password: Password)
    suspend fun deletePassword(id: Long)
}
