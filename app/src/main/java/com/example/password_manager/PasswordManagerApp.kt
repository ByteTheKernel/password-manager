package com.example.password_manager

import android.app.Application
import com.example.password_manager.data.di.DataModule
import com.example.password_manager.di.AppComponent
import com.example.password_manager.di.AppModule
import com.example.password_manager.di.DaggerAppComponent
import com.example.password_manager.domain.di.DomainModule

class PasswordManagerApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
