package com.example.password_manager.presentation.auth.viewModel

import androidx.lifecycle.*
import com.example.password_manager.domain.usecases.AuthenticateUseCase
import com.example.password_manager.domain.usecases.IsMasterSetUseCase
import com.example.password_manager.domain.usecases.SetMasterPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Loading : AuthState()
    object RequireCreate : AuthState()
    object RequireAuth : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel @Inject constructor(
    private val isMasterSetUseCase: IsMasterSetUseCase,
    private val setMasterPasswordUseCase: SetMasterPasswordUseCase,
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading)
    val authState: LiveData<AuthState> = _authState

    fun checkState() {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.postValue(AuthState.Loading)
            val isSet = isMasterSetUseCase()
            _authState.postValue(if (isSet) AuthState.RequireAuth else AuthState.RequireCreate)
        }
    }

    fun createMaster(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (password.length < 4) {
                _authState.postValue(AuthState.Error("Минимум 4 символа"))
                return@launch
            }
            try {
                setMasterPasswordUseCase(password)
                _authState.postValue(AuthState.Success)
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error("Ошибка создания ключа: ${e.localizedMessage}"))
            }
        }
    }

    fun authenticate(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = try {
                authenticateUseCase(password)
            } catch (e: Exception) {
                false
            }
            _authState.postValue(
                if (result) AuthState.Success else AuthState.Error("Неверный пароль")
            )
        }
    }

    fun clearError() {
        checkState()
    }
}

