package com.example.password_manager.presentation.addedit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.password_manager.domain.model.Password
import com.example.password_manager.domain.usecases.AddPasswordUseCase
import com.example.password_manager.domain.usecases.GetPasswordByIdUseCase
import com.example.password_manager.presentation.model.PasswordUiModel
import com.example.password_manager.presentation.model.toDomain
import com.example.password_manager.presentation.model.toUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditPasswordViewModel @Inject constructor(
    private val addPasswordUseCase: AddPasswordUseCase,
    private val getPasswordByIdUseCase: GetPasswordByIdUseCase
) : ViewModel() {

    private val _uiModel = MutableLiveData(PasswordUiModel())
    val uiModel: LiveData<PasswordUiModel> = _uiModel

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> = _saveResult

    fun loadPassword(id: Long) {
        viewModelScope.launch {
            val password = getPasswordByIdUseCase(id)
            _uiModel.postValue(password?.toUiModel() ?: PasswordUiModel())
        }
    }

    fun updateTitle(title: String) {
        _uiModel.value = _uiModel.value?.copy(title = title)
    }

    fun updateLogin(login: String) {
        _uiModel.value = _uiModel.value?.copy(login = login)
    }

    fun updatePassword(password: String) {
        _uiModel.value = _uiModel.value?.copy(password = password)
    }

    fun updateUrl(url: String?) {
        _uiModel.value = _uiModel.value?.copy(url = url)
    }

    fun updateDescription(description: String?) {
        _uiModel.value = _uiModel.value?.copy(description = description)
    }

    fun savePassword() {
        viewModelScope.launch {
            try {
                val entity = uiModel.value?.toDomain() ?: return@launch
                addPasswordUseCase(entity)
                _saveResult.postValue(true)
            } catch (e: Exception) {
                _saveResult.postValue(false)
            }
        }
    }
}

