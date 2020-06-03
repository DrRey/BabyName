package ru.drrey.babyname.auth.domain.interactor

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
class GetUserIdInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val authRepository: AuthRepository = mock()
    private val interactor =
        GetUserIdInteractor(
            authRepository
        )
    private val result = "userId"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(authRepository.getUserId()).thenReturn(flowOf(result))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null).collect { assert(it == result) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}