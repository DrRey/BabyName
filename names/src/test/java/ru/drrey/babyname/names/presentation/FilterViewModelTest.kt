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
import ru.drrey.babyname.names.domain.interactor.GetUnfilteredNamesInteractor
import ru.drrey.babyname.names.domain.interactor.SetNameFilterInteractor

@RunWith(MockitoJUnitRunner::class)
class FilterViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val namesList = listOf(Name("name1"), Name("name2"), Name("name3"))
    private val getUnfilteredNamesInteractor: GetUnfilteredNamesInteractor = mock()
    private val setNameFilterInteractor: SetNameFilterInteractor = mock()
    private val viewModel =
        spy(FilterViewModel(getUnfilteredNamesInteractor, setNameFilterInteractor))
    private val stateObserver: Observer<FilterViewState> = mock()
    private val eventObserver: Observer<FilterViewEvent> = mock()
    private val namesListObserver: Observer<List<Name>> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
        viewModel.getNamesList().observeForever(namesListObserver)
    }

    @Test
    fun filterTrue() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(List<Name>) -> Unit>().run {
                verify(getUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(namesList)
            }
            viewModel.onNameFiltered(namesList[0], true)
            verify(setNameFilterInteractor).execute(
                any(),
                argWhere { it.name == namesList[0] && it.allow },
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
            argumentCaptor<FilterViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { currentName == namesList[0] && currentNamePosition == 0 })
                assert(thirdValue.run { currentName == namesList[1] && currentNamePosition == 1 })
            }
            assert(
                viewModel.getNamesMap()[namesList[0]] == true &&
                        viewModel.getNamesMap()[namesList[1]] == null &&
                        viewModel.getNamesMap()[namesList[2]] == null
            )
        }
    }

    @Test
    fun filterFalse() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(List<Name>) -> Unit>().run {
                verify(getUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(namesList)
            }
            viewModel.onNameFiltered(namesList[0], false)
            verify(setNameFilterInteractor).execute(
                any(),
                argWhere { it.name == namesList[0] && !it.allow },
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
            argumentCaptor<FilterViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { currentName == namesList[0] && currentNamePosition == 0 })
                assert(thirdValue.run { currentName == namesList[1] && currentNamePosition == 1 })
            }
            assert(
                viewModel.getNamesMap()[namesList[0]] == false &&
                        viewModel.getNamesMap()[namesList[1]] == null &&
                        viewModel.getNamesMap()[namesList[2]] == null
            )
        }
    }

    @Test
    fun filterAllNames() {
        runBlockingTest {
            viewModel.loadNames()
            argumentCaptor<(List<Name>) -> Unit>().run {
                verify(getUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(namesList)
            }
            viewModel.onNameFiltered(namesList[0], false)
            viewModel.onNameFiltered(namesList[1], true)
            viewModel.onNameFiltered(namesList[2], false)
            argumentCaptor<FilterViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue == FilterViewEvent.NoNamesLeft)
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}