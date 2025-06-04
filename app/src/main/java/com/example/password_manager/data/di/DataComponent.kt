package com.example.password_manager.data.di

import com.example.password_manager.data.repository.AuthRepositoryImpl
import com.example.password_manager.data.repository.PasswordsRepositoryImpl
import dagger.Subcomponent


@Subcomponent
interface DataComponent {
    @Subcomponent.Factory
    interface Factory { fun create(): DataComponent }

    fun inject(repository: AuthRepositoryImpl)
    fun inject(repository: PasswordsRepositoryImpl)
}