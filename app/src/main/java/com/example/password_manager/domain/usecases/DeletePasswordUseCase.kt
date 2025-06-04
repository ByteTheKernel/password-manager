package com.example.password_manager.domain.usecases

import com.example.password_manager.domain.repository.PasswordsRepository

class DeletePasswordUseCase(
    private val repo: PasswordsRepository
) {
    suspend operator fun invoke(id: Long) = repo.deletePassword(id)
}
