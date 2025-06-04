package com.example.password_manager.di

import com.example.password_manager.data.di.DataComponent
import com.example.password_manager.data.di.DataModule
import com.example.password_manager.domain.di.DomainComponent
import com.example.password_manager.domain.di.DomainModule
import com.example.password_manager.presentation.di.PresentationComponent
import com.example.password_manager.presentation.di.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    DataModule::class,
    DomainModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun dataComponent(): DataComponent.Factory
    fun domainComponent(): DomainComponent.Factory
    fun presentationComponent(): PresentationComponent.Factory
}

