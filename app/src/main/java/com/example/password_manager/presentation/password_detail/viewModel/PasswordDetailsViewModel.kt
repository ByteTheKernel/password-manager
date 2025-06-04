package com.example.password_manager.presentation.password_detail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.password_manager.domain.usecases.DeletePasswordUseCase
import com.example.password_manager.domain.usecases.GetPasswordByIdUseCase
import com.example.password_manager.presentation.model.PasswordUiModel
import com.example.password_manager.presentation.model.toUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PasswordDetailsState {
    object Loading : PasswordDetailsState()
    data class Success(val password: PasswordUiModel) : PasswordDetailsState()
    data class Error(val message: String) : PasswordDetailsState()
    object Deleted : PasswordDetailsState()
}


class PasswordDetailsViewModel @Inject constructor(
    private val getPasswordByIdUseCase: GetPasswordByIdUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase
) : ViewModel() {

    private val _state = MutableLiveData<PasswordDetailsState>()
    val state: LiveData<PasswordDetailsState> = _state

    fun loadPassword(id: Long) {
        _state.value = PasswordDetailsState.Loading
        viewModelScope.launch {
            try {
                val password = getPasswordByIdUseCase(id)
                if (password != null) {
                    _state.value = PasswordDetailsState.Success(password.toUiModel())
                } else {
                    _state.value = PasswordDetailsState.Error("Пароль не найден")
                }
            } catch (e: Exception) {
                _state.value = PasswordDetailsState.Error(e.localizedMessage ?: "Ошибка загрузки")
            }
        }
    }

    fun deletePassword(id: Long) {
        viewModelScope.launch {
            try {
                deletePasswordUseCase(id)
                _state.value = PasswordDetailsState.Deleted
            } catch (e: Exception) {
                _state.value = PasswordDetailsState.Error(e.localizedMessage ?: "Ошибка удаления")
            }
        }
    }
}

