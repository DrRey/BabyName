package ru.drrey.babyname.names.domain.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
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
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

@RunWith(MockitoJUnitRunner::class)
class SetNameFilterInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val name = Name("name1")
    private val getUserId = { flowOf(userId) }
    private val namesRepository: NamesRepository = mock()
    private val interactor = SetNameFilterInteractor(namesRepository, getUserId)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(namesRepository.setNameFilter(eq(userId), any(), any())).thenReturn(flowOf(Unit))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(SetNameFilterInteractor.Params(name, true))
                .collect { assert(true) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}