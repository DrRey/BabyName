package ru.drrey.babyname.presentation

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
import ru.drrey.babyname.domain.interactor.CheckWelcomeScreenShownInteractor
import ru.drrey.babyname.names.api.Sex

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val sexFilter = Sex.GIRL
    private val partnerIdList = listOf("partner1, partner2")
    private val unfilteredNamesCount = 100500
    private val starredNamesCount = 100501
    private val checkWelcomeScreenShownInteractor: CheckWelcomeScreenShownInteractor = mock()
    private val getUserIdInteractor: Interactor<String, Nothing?> = mock()
    private val getPartnerIdsListInteractor: Interactor<List<String>, Nothing?> = mock()
    private val clearPartnersInteractor: Interactor<Unit, Nothing?> = mock()
    private val getSexFilterInteractor: Interactor<Sex?, Nothing?> = mock()
    private val setSexFilterInteractor: Interactor<Unit, Sex?> = mock()
    private val countStarredNamesInteractor: Interactor<Int, Nothing?> = mock()
    private val countUnfilteredNamesInteractor: Interactor<Int, Nothing?> = mock()
    private val viewModel = spy(
        MainViewModel(
            checkWelcomeScreenShownInteractor,
            getUserIdInteractor,
            getPartnerIdsListInteractor,
            clearPartnersInteractor,
            getSexFilterInteractor,
            setSexFilterInteractor,
            countStarredNamesInteractor,
            countUnfilteredNamesInteractor
        )
    )
    private val stateObserver: Observer<MainViewState> = mock()
    private val eventObserver: Observer<MainViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun startWelcome() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(false)
            }
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue == MainViewEvent.WelcomeScreenNeeded)
            }
        }
    }

    @Test
    fun loadUserError() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { !isLoadingData && error == "123" })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun loadUserNotLoggedIn() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { !isLoadingData && error == null && !showOverlay })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun loadSexFilter() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(4)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { isLoggedIn })
                assert(allValues[3].run { sexFilterLoaded && sexFilter == this@MainViewModelTest.sexFilter })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun loadPartnersError() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
            argumentCaptor<(Exception) -> Unit>().run {
                verify(getPartnerIdsListInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(4)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { isLoggedIn })
                assert(allValues[3].run { !isLoadingData && error == "123" })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun countUnfilteredNamesError() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
                firstValue.invoke(partnerIdList)
            }
            argumentCaptor<(Exception) -> Unit>().run {
                verify(countUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(5)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { isLoggedIn })
                assert(allValues[3].run { partnersCount == partnerIdList.count() })
                assert(allValues[4].run { !isLoadingData && error == "123" })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun countStarredNamesError() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
                firstValue.invoke(partnerIdList)
            }
            argumentCaptor<(Int) -> Unit>().run {
                verify(countUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(unfilteredNamesCount)
            }
            argumentCaptor<(Exception) -> Unit>().run {
                verify(countStarredNamesInteractor).execute(
                    any(),
                    eq(null),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(6)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { isLoggedIn })
                assert(allValues[3].run { partnersCount == partnerIdList.count() })
                assert(allValues[4].run { unfilteredNamesCount == this@MainViewModelTest.unfilteredNamesCount })
                assert(allValues[5].run { !isLoadingData && error == "123" })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun loadAllSuccess() {
        runBlockingTest {
            viewModel.loadData()
            argumentCaptor<(Boolean) -> Unit>().run {
                verify(checkWelcomeScreenShownInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(true)
            }
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
                firstValue.invoke(partnerIdList)
            }
            argumentCaptor<(Int) -> Unit>().run {
                verify(countUnfilteredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(unfilteredNamesCount)
            }
            argumentCaptor<(Int) -> Unit>().run {
                verify(countStarredNamesInteractor).execute(
                    any(),
                    eq(null),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(starredNamesCount)
            }
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(7)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { isLoadingData })
                assert(thirdValue.run { isLoggedIn })
                assert(allValues[3].run { partnersCount == partnerIdList.count() })
                assert(allValues[4].run { unfilteredNamesCount == this@MainViewModelTest.unfilteredNamesCount })
                assert(allValues[5].run { starredNamesCount == this@MainViewModelTest.starredNamesCount })
                assert(allValues[6].run { !isLoadingData && error == null })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @Test
    fun setSexFilter() {
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
            verify(checkWelcomeScreenShownInteractor).execute(
                any(),
                eq(null),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
            argumentCaptor<MainViewState>().run {
                verify(stateObserver, times(3)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { sexFilterLoaded && sexFilter == this@MainViewModelTest.sexFilter })
                assert(thirdValue.run { isLoadingData })
            }
            argumentCaptor<MainViewEvent>().run {
                verify(eventObserver, times(0)).onChanged(capture())
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}