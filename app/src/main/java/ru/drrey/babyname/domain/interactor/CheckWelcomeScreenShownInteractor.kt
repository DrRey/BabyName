package ru.drrey.babyname.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.domain.repository.MainRepository

/**
 * Check if welcome screen was shown interactor
 */
class CheckWelcomeScreenShownInteractor(
    private val mainRepository: MainRepository
) : Interactor<Boolean, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<Boolean> {
        return mainRepository.checkFirstStart()
    }
}