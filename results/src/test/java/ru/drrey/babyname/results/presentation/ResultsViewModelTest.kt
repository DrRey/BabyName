package ru.drrey.babyname.results.presentation

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
import ru.drrey.babyname.results.domain.entity.Result
import ru.drrey.babyname.results.domain.interactor.GetResultsInteractor

@RunWith(MockitoJUnitRunner::class)
class ResultsViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val results = listOf(Result("name1", 1f), Result("name2", 5f))
    private val getResultsInteractor: GetResultsInteractor = mock()
    private val viewModel = spy(ResultsViewModel(getResultsInteractor))
    private val stateObserver: Observer<ResultsViewState> = mock()
    private val eventObserver: Observer<ResultsViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun loadResultsError() {
        runBlockingTest {
            viewModel.loadResults()
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getResultsInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<ResultsViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && results == null && error == null })
                assert(thirdValue.run { isLoaded && !isLoading && results == null && error == "123" })
            }
        }
    }

    @Test
    fun loadResultsSuccess() {
        runBlockingTest {
            viewModel.loadResults()
            argumentCaptor<(List<Result>) -> Unit>().run {
                verify(getResultsInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(results)
            }
            argumentCaptor<ResultsViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { !isLoaded && isLoading && results == null && error == null })
                assert(thirdValue.run { isLoaded && !isLoading && results == this@ResultsViewModelTest.results && error == null })
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}