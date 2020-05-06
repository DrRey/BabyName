package ru.drrey.babyname.theme.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.theme.domain.repository.ThemeRepository

/**
 * Save primary color interactor
 */
class SavePrimaryColorInteractor(
    private val themeRepository: ThemeRepository
) : Interactor<Unit, Int>() {

    override fun buildFlow(params: Int): Flow<Unit> {
        return themeRepository.savePrimaryColor(params)
    }
}