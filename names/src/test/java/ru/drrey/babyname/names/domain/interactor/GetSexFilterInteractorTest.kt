package ru.drrey.babyname.names.domain.interactor

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
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.repository.NamesRepository

@RunWith(MockitoJUnitRunner::class)
class GetSexFilterInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val sexFilter = Sex.GIRL
    private val namesRepository: NamesRepository = mock()
    private val getUserId = { flowOf(userId) }
    private val interactor =
        GetSexFilterInteractor(namesRepository, getUserId)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(namesRepository.getSexFilter(userId)).thenReturn(flowOf(sexFilter))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null).collect { assert(it == sexFilter) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}