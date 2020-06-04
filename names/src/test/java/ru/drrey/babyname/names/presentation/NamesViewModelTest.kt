package ru.drrey.babyname.names.presentation

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
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.interactor.GetFilteredNamesInteractor
import ru.drrey.babyname.names.domain.interactor.SetStarsInteractor

@RunWith(MockitoJUnitRunner::class)
class NamesViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val namesList = listOf(Name("name1"), Name("name2"), Name("name3"))
    private val getFilteredNamesInteractor: GetFilteredNamesInteractor = mock()
    private val setStarsInteractor: SetStarsInteractor = mock()
    private val viewModel =
        spy(NamesViewModel(getFilteredNamesInteractor, setStarsInteractor))
    private val stateObserver: Observer<NamesViewState> = mock()
    private val eventObserver: Observer<NamesViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun loadNamesError() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getFilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<NamesViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && loadError == null && names == null })
                assert(thirdValue.run { isLoaded && !isLoading && loadError == "123" && names == null })
            }
        }
    }

    @Test
    fun loadNamesAndSetStarsSuccess() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(List<Name>) -> Unit>().run {
                verify(getFilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(namesList)
            }
            viewModel.setStars(namesList[0], 0, 5)
            argumentCaptor<NamesViewState>().run {
                verify(stateObserver, times(4)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && loadError == null && names == null })
                assert(thirdValue.run { isLoaded && !isLoading && loadError == null && names == namesList })
                assert(allValues[3].run {
                    isLoaded && !isLoading && loadError == null && names?.get(0)?.stars == 5
                })
            }
        }
    }

    @Test
    fun loadNamesAndSetStarsError() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(List<Name>) -> Unit>().run {
                verify(getFilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(namesList)
            }
            viewModel.setStars(namesList[0], 0, 5)
            argumentCaptor<(Exception) -> Unit>().run {
                verify(setStarsInteractor).execute(
                    any(),
                    argThat { name == namesList[0] && stars == 5 },
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<NamesViewState>().run {
                verify(stateObserver, times(5)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && loadError == null && names == null })
                assert(thirdValue.run { isLoaded && !isLoading && loadError == null && names == namesList })
                assert(allValues[3].run {
                    isLoaded && !isLoading && loadError == null && names?.get(0)?.stars == 5
                })
                assert(allValues[4].run {
                    isLoaded && !isLoading && loadError == null && names?.get(0)?.stars == null
                })
            }
            argumentCaptor<NamesViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue is NamesViewEvent.StarsSetError)
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}