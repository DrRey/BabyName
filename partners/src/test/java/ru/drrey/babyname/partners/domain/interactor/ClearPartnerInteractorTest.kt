package ru.drrey.babyname.partners.domain.interactor

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
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

@RunWith(MockitoJUnitRunner::class)
class ClearPartnerInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val partnersRepository: PartnersRepository = mock()
    private val getUserId = { flowOf(userId) }
    private val interactor = ClearPartnersInteractor(partnersRepository, getUserId)
    private val userId = "userId"
    private val partnersList = listOf(Partner("1"), Partner("2"))

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(partnersRepository.clearPartners(userId, partnersList)).thenReturn(flowOf(Unit))
        whenever(partnersRepository.getPartnersList(userId)).thenReturn(flowOf(partnersList))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(null).collect { assert(true) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}