package ru.drrey.babyname.theme.domain.interactor

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
import ru.drrey.babyname.theme.domain.repository.ThemeRepository

@RunWith(MockitoJUnitRunner::class)
class SaveAccentColorInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val themeRepository: ThemeRepository = mock()
    private val interactor = SaveAccentColorInteractor(themeRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(themeRepository.saveAccentColor(any())).thenReturn(flowOf(Unit))
    }

    @Test
    fun testFlow() {
        runBlockingTest {
            interactor.buildFlow(111).collect { assert(true) }
        }
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}