package ru.drrey.babyname.theme.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.theme.domain.repository.ThemeRepository

/**
 * Get primary color interactor
 */
class GetPrimaryColorInteractor(
    private val themeRepository: ThemeRepository
) : Interactor<Int?, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<Int?> {
        return themeRepository.getPrimaryColor()
    }
}