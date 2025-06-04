package com.example.password_manager.domain.repository

interface AuthRepository {
    suspend fun isMasterSet(): Boolean
    suspend fun setMaster(password: String)
    suspend fun authenticate(password: String): Boolean
}