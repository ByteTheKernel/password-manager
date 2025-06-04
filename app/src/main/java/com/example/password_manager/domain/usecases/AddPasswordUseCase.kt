package com.example.password_manager.domain.usecases

import com.example.password_manager.domain.model.Password
import com.example.password_manager.domain.repository.PasswordsRepository

class AddPasswordUseCase(private val repo: PasswordsRepository) {
    suspend operator fun invoke(password: Password) = repo.addPassword(password)
}