package ru.drrey.babyname.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.domain.repository.MainRepository

/**
 * Check if welcome screen was shown interactor
 */
class CheckWelcomeScreenShownInteractor(
    private val mainRepository: MainRepository
) : Interactor<Boolean, Void?>() {

    override fun buildFlow(params: Void?): Flow<Boolean> {
        return mainRepository.checkFirstStart()
    }
}