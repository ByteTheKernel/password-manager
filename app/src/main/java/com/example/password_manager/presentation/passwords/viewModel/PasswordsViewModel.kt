package com.example.password_manager.presentation.passwords.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.password_manager.domain.repository.PasswordsRepository
import com.example.password_manager.domain.usecases.GetPasswordPreviewsPagingUseCase
import com.example.password_manager.domain.usecases.GetPasswordPreviewsUseCase
import com.example.password_manager.domain.usecases.SearchPasswordPreviewsUseCase
import com.example.password_manager.presentation.passwords.model.PasswordListItem
import com.example.password_manager.presentation.passwords.model.toListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class PasswordsViewModel @Inject constructor(
    private val getPasswordPreviewsPagingUseCase: GetPasswordPreviewsPagingUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val passwordsPagingFlow: Flow<PagingData<PasswordListItem>> =
        searchQuery
            .debounce(200)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                getPasswordPreviewsPagingUseCase(query) // Возвращает Flow<PagingData<PasswordPreview>>
            }
            .map { pagingData -> pagingData.map { it.toListItem() } } // Domain → UI
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.IO)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}



