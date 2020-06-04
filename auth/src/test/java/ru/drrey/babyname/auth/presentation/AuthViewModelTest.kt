package ru.drrey.babyname.auth.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.drrey.babyname.auth.api.NotLoggedInException
import ru.drrey.babyname.auth.domain.interactor.GetUserIdInteractor
import ru.drrey.babyname.auth.domain.interactor.SetUserIdInteractor

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val getUserIdInteractor: GetUserIdInteractor = mock()
    private val setUserIdInteractor: SetUserIdInteractor = mock()
    private val viewModel = spy(AuthViewModel(getUserIdInteractor, setUserIdInteractor))
    private val stateObserver: Observer<AuthViewState> = mock()
    private val eventObserver: Observer<AuthViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun authLoadSuccess() {
        runBlockingTest {
            viewModel.loadAuth()
            argumentCaptor<(String) -> Unit>().run {
                verify(getUserIdInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(userId)
            }
            argumentCaptor<AuthViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && error == null && userId == null })
                assert(thirdValue.run { isLoaded && !isLoading && error == null && userId == userId })
            }
        }
    }

    @Test
    fun authLoadError() {
        runBlockingTest {
            viewModel.loadAuth()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getUserIdInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<AuthViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && error == null && userId == null })
                assert(thirdValue.run { isLoaded && !isLoading && error == "123" && userId == null })
            }
        }
    }

    @Test
    fun authLoginSuccess() {
        runBlockingTest {
            viewModel.loadAuth()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getUserIdInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(NotLoggedInException())
            }
            viewModel.onAuthComplete(userId)
            argumentCaptor<(Unit) -> Unit>().run {
                verify(setUserIdInteractor).execute(
                    any(),
                    eq(userId),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(Unit)
            }
            argumentCaptor<AuthViewState>().run {
                verify(stateObserver, times(4)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && error == null && userId == null })
                assert(thirdValue.run { isLoaded && !isLoading && error == null && userId == null })
                assert(allValues[3].run { isLoaded && !isLoading && error == null && userId == this@AuthViewModelTest.userId })
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}