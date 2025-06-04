package com.example.password_manager.domain.usecases

import com.example.password_manager.domain.repository.AuthRepository

class AuthenticateUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(password: String): Boolean = repo.authenticate(password)
}
