package com.example.password_manager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.password_manager.data.source.datastore.PrefsManager
import com.example.password_manager.data.source.local.AppDatabase
import com.example.password_manager.data.source.local.PasswordDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides @Singleton
    fun providePrefsManager(context: Context): PrefsManager = PrefsManager(context)

    @Provides @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "passwords_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides @Singleton
    fun providePasswordDao(db: AppDatabase): PasswordDao = db.passwordDao()
}
