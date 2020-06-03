package ru.drrey.babyname.results.domain.interactor

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

@RunWith(MockitoJUnitRunner::class)
class GetResultsInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val userId = "userId"
    private val partnersList = listOf("partner1")
    private val starsList = listOf(NameStars("name1", 5), NameStars("name2", 1))
    private val partnerStarsList = listOf(NameStars("name1", 5), NameStars("name2", 5))
    private val getUserId = { flowOf(userId) }
    private val getPartners = { _: String -> flowOf(partnersList) }
    private val getStars = { _: String -> flowOf(starsList) }
    private val getPartnerStars =
        { _: List<String> -> flowOf(Pair(partnersList[0], partnerStarsList)) }
    private val interactor = GetResultsInteractor(getUserId, getStars, getPartners, getPartnerStars)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null)
                .collect {
                    assert(
                        it[0].name == "name1" && it[0].averageStars == 5f &&
                                it[1].name == "name2" && it[1].averageStars == 3f
                    )
                }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}