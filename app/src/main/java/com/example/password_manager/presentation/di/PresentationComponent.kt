package com.example.password_manager.presentation.di

import dagger.Subcomponent

@Subcomponent
interface PresentationComponent {
    @Subcomponent.Factory
    interface Factory { fun create(): PresentationComponent }

    fun daggerViewModelFactory(): DaggerViewModelFactory
}