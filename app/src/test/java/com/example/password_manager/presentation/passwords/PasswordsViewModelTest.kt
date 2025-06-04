package com.example.password_manager.presentation.passwords

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.password_manager.domain.usecases.GetPasswordPreviewsPagingUseCase
import com.example.password_manager.presentation.passwords.viewModel.PasswordsViewModel
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.After
import kotlinx.coroutines.test.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import kotlinx.coroutines.flow.flowOf
import androidx.paging.PagingData
import com.example.password_manager.domain.model.PasswordPreview
import io.mockk.*


@OptIn(ExperimentalCoroutinesApi::class)
class PasswordsViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getPasswordPreviewsPagingUseCase = mockk<GetPasswordPreviewsPagingUseCase>()
    private lateinit var viewModel: PasswordsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val testItems = listOf(
            PasswordPreview(id = 1, title = "test", url = "test.com")
        )
        every { getPasswordPreviewsPagingUseCase.invoke(any()) } returns flowOf(PagingData.from(testItems))

        viewModel = PasswordsViewModel(getPasswordPreviewsPagingUseCase)
    }

    @Test
    fun `setSearchQuery updates searchQuery LiveData`() {
        viewModel.setSearchQuery("test")
        assertEquals("test", viewModel.searchQuery.value)
    }
}

