package com.example.password_manager.domain.di

import com.example.password_manager.domain.repository.AuthRepository
import com.example.password_manager.domain.repository.PasswordsRepository
import com.example.password_manager.domain.usecases.AddPasswordUseCase
import com.example.password_manager.domain.usecases.AuthenticateUseCase
import com.example.password_manager.domain.usecases.DeletePasswordUseCase
import com.example.password_manager.domain.usecases.GetPasswordByIdUseCase
import com.example.password_manager.domain.usecases.GetPasswordPreviewsPagingUseCase
import com.example.password_manager.domain.usecases.GetPasswordPreviewsUseCase
import com.example.password_manager.domain.usecases.IsMasterSetUseCase
import com.example.password_manager.domain.usecases.SearchPasswordPreviewsUseCase
import com.example.password_manager.domain.usecases.SetMasterPasswordUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideIsMasterSetUseCase(repo: AuthRepository): IsMasterSetUseCase =
        IsMasterSetUseCase(repo)

    @Provides
    fun provideSetMasterPasswordUseCase(repo: AuthRepository): SetMasterPasswordUseCase =
        SetMasterPasswordUseCase(repo)

    @Provides
    fun provideAuthenticateUseCase(repo: AuthRepository): AuthenticateUseCase =
        AuthenticateUseCase(repo)

    @Provides
    fun provideGetPasswordPreviewsUseCase(repo: PasswordsRepository): GetPasswordPreviewsUseCase =
        GetPasswordPreviewsUseCase(repo)

    @Provides
    fun provideSearchPasswordPreviewsUseCase(repo: PasswordsRepository): SearchPasswordPreviewsUseCase =
        SearchPasswordPreviewsUseCase(repo)

    @Provides
    fun provideAddPasswordUseCase(repo: PasswordsRepository): AddPasswordUseCase =
        AddPasswordUseCase(repo)

    @Provides
    fun provideGetPasswordByIdUseCase(repo: PasswordsRepository): GetPasswordByIdUseCase =
        GetPasswordByIdUseCase(repo)

    @Provides
    fun provideGetPasswordPreviewsPagingUseCase(repo: PasswordsRepository): GetPasswordPreviewsPagingUseCase =
        GetPasswordPreviewsPagingUseCase(repo)

    @Provides
    fun provideDeletePasswordUseCase(repo: PasswordsRepository) =
        DeletePasswordUseCase(repo)
}

