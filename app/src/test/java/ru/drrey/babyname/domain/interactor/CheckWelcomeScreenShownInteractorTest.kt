package ru.drrey.babyname.domain.interactor

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
import ru.drrey.babyname.domain.repository.MainRepository

@RunWith(MockitoJUnitRunner::class)
class CheckWelcomeScreenShownInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val mainRepository: MainRepository = mock()
    private val interactor = CheckWelcomeScreenShownInteractor(mainRepository)
    private val result = true

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(mainRepository.checkFirstStart()).thenReturn(flowOf(result))
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