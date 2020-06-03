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
import ru.drrey.babyname.names.domain.entity.Name

@RunWith(MockitoJUnitRunner::class)
class GetUnfilteredNamesInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val namesList = listOf(
        Name("name1", stars = -1),
        Name("name2", stars = 0),
        Name("name3", stars = 5),
        Name("name4", stars = null)
    )
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor = mock()
    private val interactor = GetUnfilteredNamesInteractor(getNamesWithStarsInteractor)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(getNamesWithStarsInteractor.buildFlow(null)).thenReturn(flowOf(namesList))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null).collect {
                assert(it.count() == 1 && it[0].displayName == "name4")
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}