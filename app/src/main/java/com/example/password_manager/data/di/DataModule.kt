package com.example.password_manager.data.di

import android.content.Context
import androidx.room.Room
import com.example.password_manager.data.repository.AuthRepositoryImpl
import com.example.password_manager.data.repository.PasswordsRepositoryImpl
import com.example.password_manager.data.source.datastore.PrefsManager
import com.example.password_manager.data.source.local.AppDatabase
import com.example.password_manager.data.source.local.PasswordDao
import com.example.password_manager.domain.repository.AuthRepository
import com.example.password_manager.domain.repository.PasswordsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides @Singleton
    fun provideAuthRepository(prefsManager: PrefsManager): AuthRepository =
        AuthRepositoryImpl(prefsManager)

    @Provides @Singleton
    fun providePasswordsRepository(dao: PasswordDao): PasswordsRepository =
        PasswordsRepositoryImpl(dao)
}