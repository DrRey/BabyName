package ru.drrey.babyname.theme.presentation

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
import ru.drrey.babyname.theme.R
import ru.drrey.babyname.theme.api.ThemeViewEvent
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.domain.interactor.GetAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.GetPrimaryColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SaveAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SavePrimaryColorInteractor

@RunWith(MockitoJUnitRunner::class)
class ThemeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val primaryColor = 123
    private val accentColor = 456
    private val getPrimaryColorInteractor: GetPrimaryColorInteractor = mock()
    private val getAccentColorInteractor: GetAccentColorInteractor = mock()
    private val savePrimaryColorInteractor: SavePrimaryColorInteractor = mock()
    private val saveAccentColorInteractor: SaveAccentColorInteractor = mock()
    private val viewModel = spy(
        ThemeViewModel(
            getPrimaryColorInteractor,
            getAccentColorInteractor,
            savePrimaryColorInteractor,
            saveAccentColorInteractor
        )
    )
    private val stateObserver: Observer<ThemeViewState> = mock()
    private val eventObserver: Observer<ThemeViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun initPrimaryError() {
        runBlockingTest {
            viewModel.init()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getPrimaryColorInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<ThemeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { primaryColorResId == R.color.colorPrimary })
            }
        }
    }

    @Test
    fun initAccentError() {
        runBlockingTest {
            viewModel.init()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getAccentColorInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<ThemeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { accentColorResId == R.color.colorAccent })
            }
        }
    }

    @Test
    fun initSuccess() {
        runBlockingTest {
            viewModel.init()
            argumentCaptor<(Int?) -> Unit>().run {
                verify(getPrimaryColorInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(primaryColor)
            }
            argumentCaptor<(Int?) -> Unit>().run {
                verify(getAccentColorInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(accentColor)
            }
            argumentCaptor<ThemeViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { primaryColorResId == primaryColor })
                assert(thirdValue.run { primaryColorResId == primaryColor && accentColorResId == accentColor })
            }
        }
    }

    @Test
    fun primaryChange() {
        runBlockingTest {
            viewModel.onPrimaryColorChange(primaryColor)
            verify(savePrimaryColorInteractor).execute(
                any(),
                eq(primaryColor),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
            argumentCaptor<ThemeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { primaryColorResId == primaryColor })
            }
        }
    }

    @Test
    fun accentChange() {
        runBlockingTest {
            viewModel.onAccentColorChange(accentColor)
            verify(saveAccentColorInteractor).execute(
                any(),
                eq(accentColor),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
            argumentCaptor<ThemeViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { accentColorResId == accentColor })
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}