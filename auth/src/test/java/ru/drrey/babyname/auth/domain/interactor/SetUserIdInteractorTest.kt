package ru.drrey.babyname.auth.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.drrey.babyname.auth.domain.repository.AuthRepository

@RunWith(MockitoJUnitRunner::class)
class SetUserIdInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val authRepository: AuthRepository = mock()
    private val interactor = SetUserIdInteractor(authRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(authRepository.setUserId(any())).thenReturn(flowOf(Unit))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow("userId").collect { assert(true) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}