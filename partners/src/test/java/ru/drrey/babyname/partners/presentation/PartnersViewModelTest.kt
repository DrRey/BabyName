package ru.drrey.babyname.partners.presentation

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
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.partners.domain.interactor.AddPartnerInteractor

@RunWith(MockitoJUnitRunner::class)
class PartnersViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val getUserIdInteractor: Interactor<String, Nothing?> = mock()
    private val addPartnerInteractor: AddPartnerInteractor = mock()
    private val viewModel =
        spy(PartnersViewModel(getUserIdInteractor, addPartnerInteractor))
    private val stateObserver: Observer<PartnersViewState> = mock()
    private val eventObserver: Observer<PartnersViewEvent> = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        doReturn(true).whenever(viewModel).isMainThread()
        viewModel.getViewState().observeForever(stateObserver)
        viewModel.getViewEvent().observeForever(eventObserver)
    }

    @Test
    fun loadUserError() {
        runBlockingTest {
            viewModel.loadUserData()
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
            argumentCaptor<PartnersViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { userId == null && loadUserError == "123" })
            }
        }
    }

    @Test
    fun loadUserAndAddPartnerError() {
        runBlockingTest {
            viewModel.loadUserData()
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
            viewModel.onAddPartner("partnerId")
            argumentCaptor<(Exception) -> Unit>().run {
                verify(addPartnerInteractor).execute(
                    any(),
                    eq("partnerId"),
                    capture(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
                firstValue.invoke(Exception("123"))
            }
            argumentCaptor<PartnersViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { userId == this@PartnersViewModelTest.userId && loadUserError == null })
            }
            argumentCaptor<PartnersViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue is PartnersViewEvent.PartnerAddError)
            }
        }
    }

    @Test
    fun loadUserAndAddPartnerSuccess() {
        runBlockingTest {
            viewModel.loadUserData()
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
            viewModel.onAddPartner("partnerId")
            argumentCaptor<(Unit) -> Unit>().run {
                verify(addPartnerInteractor).execute(
                    any(),
                    eq("partnerId"),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    capture()
                )
                firstValue.invoke(Unit)
            }
            argumentCaptor<PartnersViewState>().run {
                verify(stateObserver, times(2)).onChanged(capture())
                assert(firstValue == viewModel.initialViewState)
                assert(secondValue.run { userId == this@PartnersViewModelTest.userId && loadUserError == null })
            }
            argumentCaptor<PartnersViewEvent>().run {
                verify(eventObserver, times(1)).onChanged(capture())
                assert(firstValue is PartnersViewEvent.PartnerAdded)
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}