package com.example.password_manager.domain.usecases

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.password_manager.domain.model.PasswordPreview
import com.example.password_manager.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class GetPasswordPreviewsPagingUseCase(
    private val repository: PasswordsRepository
) {
    operator fun invoke(query: String): Flow<PagingData<PasswordPreview>> =
        repository.getPasswordPreviewsPaging(query)
}

