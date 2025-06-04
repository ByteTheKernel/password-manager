package com.example.password_manager.domain.usecases


import com.example.password_manager.domain.model.PasswordPreview
import com.example.password_manager.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class SearchPasswordPreviewsUseCase(private val repo: PasswordsRepository) {
    suspend operator fun invoke(query: String): Flow<List<PasswordPreview>> = repo.searchPasswordPreviews(query)
}