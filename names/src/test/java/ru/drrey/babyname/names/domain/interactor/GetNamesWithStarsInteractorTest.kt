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
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

@RunWith(MockitoJUnitRunner::class)
class GetNamesWithStarsInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val sexFilter = Sex.GIRL
    private val namesList = listOf(
        Name("name1", sexString = "М"),
        Name("name2", sexString = "Ж"),
        Name("name3", sexString = "Ж")
    )
    private val nameStarsList =
        listOf(NameStars("name1", 1), NameStars("name2", 3), NameStars("name3", 5))
    private val namesRepository: NamesRepository = mock()
    private val getUserId = { flowOf(userId) }
    private val getSexFilterInteractor: GetSexFilterInteractor = mock()
    private val interactor =
        GetNamesWithStarsInteractor(namesRepository, getUserId, getSexFilterInteractor)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(getSexFilterInteractor.buildFlow(null)).thenReturn(flowOf(sexFilter))
        whenever(namesRepository.getNames()).thenReturn(flowOf(namesList))
        whenever(namesRepository.getStars(userId)).thenReturn(flowOf(nameStarsList))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null).collect {
                assert(
                    it.count() == 2 &&
                            it[0].displayName == "name2" && it[0].stars == 3 &&
                            it[1].displayName == "name3" && it[1].stars == 5
                )
            }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}