package com.example.password_manager.domain.di

import dagger.Subcomponent

@Subcomponent
interface DomainComponent {
    @Subcomponent.Factory
    interface Factory { fun create(): DomainComponent }

}