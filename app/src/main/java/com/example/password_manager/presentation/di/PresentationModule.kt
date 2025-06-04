package com.example.password_manager.presentation.di

import androidx.lifecycle.ViewModel
import com.example.password_manager.presentation.addedit.viewModel.AddEditPasswordViewModel
import com.example.password_manager.presentation.auth.viewModel.AuthViewModel
import com.example.password_manager.presentation.password_detail.viewModel.PasswordDetailsViewModel
import com.example.password_manager.presentation.passwords.viewModel.PasswordsViewModel
import com.example.password_manager.presentation.settings.vewModel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(vm: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PasswordsViewModel::class)
    abstract fun bindPasswordsViewModel(vm: PasswordsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditPasswordViewModel::class)
    abstract fun bindAddPasswordViewModel(vm: AddEditPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PasswordDetailsViewModel::class)
    abstract fun bindPasswordDetailsViewModel(vm: PasswordDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(vm: SettingsViewModel): ViewModel

}
