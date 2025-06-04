package com.example.password_manager.presentation.settings.vewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.password_manager.domain.usecases.AuthenticateUseCase
import com.example.password_manager.domain.usecases.SetMasterPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val setMasterPasswordUseCase: SetMasterPasswordUseCase
) : ViewModel() {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Success : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> = _state

    fun changeMasterPassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(State.Loading)
            val authenticated = try {
                authenticateUseCase(oldPassword)
            } catch (e: Exception) {
                false
            }
            if (!authenticated) {
                _state.postValue(State.Error("Старый пароль неверен"))
                return@launch
            }
            if (newPassword.length < 4) {
                _state.postValue(State.Error("Новый пароль слишком короткий"))
                return@launch
            }
            try {
                setMasterPasswordUseCase(newPassword)
                _state.postValue(State.Success)
            } catch (e: Exception) {
                _state.postValue(State.Error("Ошибка при смене пароля: ${e.localizedMessage}"))
            }
        }
    }

    fun reset() {
        _state.value = State.Idle
    }
}
