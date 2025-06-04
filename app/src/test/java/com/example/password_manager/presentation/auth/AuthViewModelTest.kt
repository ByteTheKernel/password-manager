import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.password_manager.domain.usecases.AuthenticateUseCase
import com.example.password_manager.domain.usecases.IsMasterSetUseCase
import com.example.password_manager.domain.usecases.SetMasterPasswordUseCase
import com.example.password_manager.presentation.auth.viewModel.AuthState
import com.example.password_manager.presentation.auth.viewModel.AuthViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val isMasterSetUseCase = mockk<IsMasterSetUseCase>()
    private val setMasterPasswordUseCase = mockk<SetMasterPasswordUseCase>(relaxed = true)
    private val authenticateUseCase = mockk<AuthenticateUseCase>()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AuthViewModel(isMasterSetUseCase, setMasterPasswordUseCase, authenticateUseCase)
    }

    @Test
    fun `checkState sets RequireAuth when master set`() = runTest {
        coEvery { isMasterSetUseCase.invoke() } returns true
        viewModel.checkState()
        Thread.sleep(10)
        assertTrue(viewModel.authState.value is AuthState.RequireAuth)
    }

    @Test
    fun `checkState sets RequireCreate when master not set`() = runTest {
        coEvery { isMasterSetUseCase.invoke() } returns false
        viewModel.checkState()
        Thread.sleep(10)
        assertTrue(viewModel.authState.value is AuthState.RequireCreate)
    }

    @Test
    fun `createMaster posts error if password too short`() = runTest {
        viewModel.createMaster("abc")
        Thread.sleep(10)
        assertTrue(viewModel.authState.value is AuthState.Error)
        assertEquals("Минимум 4 символа", (viewModel.authState.value as AuthState.Error).message)
    }

    @Test
    fun `createMaster posts success if password ok`() = runTest {
        coJustRun { setMasterPasswordUseCase.invoke(any()) }
        viewModel.createMaster("abcd")
        Thread.sleep(10)
        assertTrue(viewModel.authState.value is AuthState.Success)
    }
}
