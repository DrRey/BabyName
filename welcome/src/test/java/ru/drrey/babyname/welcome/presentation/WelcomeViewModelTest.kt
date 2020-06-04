package ru.drrey.babyname.welcome.presentation

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
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.welcome.R

@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val partnersList = listOf("partner1", "partner2")
    private val sexFilter = Sex.GIRL
    private val getUserIdInteractor: Interactor<String, Nothing?> = mock()
    private val getPartnerIdsListInteractor: Interactor<List<String>, Nothing?> = mock()
    private val getSexFilterInteractor: Interactor<Sex?, Nothing?> = mock()
    private val setSexFilterInteractor: Interactor<Unit, Sex?> = mock()
    private val viewModel = spy(
        WelcomeViewModel(
            getUserIdInteractor,
            getPartnerIdsListInteractor,
            getSexFilterInteractor,
            setSexFilterInteractor
        )
    )
    private val stateObserver: Observer<WelcomeViewState> = mock()
    private val eventObserver: Observer<WelcomeViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun authWelcome() {
        runBlockingTest {
            viewModel.startWelcome()
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
            testDispatcher.advanceUntilIdle()
            argumentCaptor<WelcomeViewState>().run {
                verify(stateObserver, times(5)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { textShown && textResId == R.string.hello })
                assert(thirdValue.run { !textShown })
                assert(allValues[3].run { textShown && textResId == R.string.welcome_auth })
                assert(allValues[4].run { !textShown })
            }
            argumentCaptor<WelcomeViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue == WelcomeViewEvent.StartAuth)
            }
        }
    }

    @Test
    fun partnerWelcome() {
        runBlockingTest {
            viewModel.startWelcome()
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
            argumentCaptor<(List<String>) -> Unit>().run {
                verify(getPartnerIdsListInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(emptyList())
            }
            testDispatcher.advanceUntilIdle()
            argumentCaptor<WelcomeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { textShown && textResId == R.string.welcome_partner && partnerButtonsShown })
            }
            argumentCaptor<WelcomeViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun sexWelcome() {
        runBlockingTest {
            viewModel.startWelcome()
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
            argumentCaptor<(List<String>) -> Unit>().run {
                verify(getPartnerIdsListInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(partnersList)
            }
            argumentCaptor<(Sex?) -> Unit>().run {
                verify(getSexFilterInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(null)
            }
            testDispatcher.advanceUntilIdle()
            argumentCaptor<WelcomeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { textShown && textResId == R.string.welcome_sex && sexButtonsShown })
            }
            argumentCaptor<WelcomeViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun onSexSet() {
        runBlockingTest {
            viewModel.onSexSet(sexFilter)
            argumentCaptor<(Unit) -> Unit>().run {
                verify(setSexFilterInteractor).execute(
                    any(),
                    eq(sexFilter),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(Unit)
            }
            testDispatcher.advanceUntilIdle()
            argumentCaptor<WelcomeViewState>().run {
                verify(stateObserver, times(5)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { textShown && textResId == R.string.congrats })
                assert(thirdValue.run { !textShown })
                assert(allValues[3].run { textShown && textResId == R.string.welcome_ready })
                assert(allValues[4].run { !textShown })
            }
            argumentCaptor<WelcomeViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue == WelcomeViewEvent.WelcomeFinished)
            }
        }
    }

    @Test
    fun welcomeFinished() {
        runBlockingTest {
            viewModel.startWelcome()
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
            argumentCaptor<(List<String>) -> Unit>().run {
                verify(getPartnerIdsListInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(partnersList)
            }
            argumentCaptor<(Sex?) -> Unit>().run {
                verify(getSexFilterInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(sexFilter)
            }
            testDispatcher.advanceUntilIdle()
            argumentCaptor<WelcomeViewState>().run {
                verify(stateObserver, times(5)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { textShown && textResId == R.string.congrats })
                assert(thirdValue.run { !textShown })
                assert(allValues[3].run { textShown && textResId == R.string.welcome_ready })
                assert(allValues[4].run { !textShown })
            }
            argumentCaptor<WelcomeViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue == WelcomeViewEvent.WelcomeFinished)
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}