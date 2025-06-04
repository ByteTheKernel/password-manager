package com.example.password_manager.domain.usecases

import com.example.password_manager.domain.repository.AuthRepository

class IsMasterSetUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): Boolean = repo.isMasterSet()
}
