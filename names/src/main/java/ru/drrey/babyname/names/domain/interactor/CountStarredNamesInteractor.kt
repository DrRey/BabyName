package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor

/**
 * Count names with stars interactor
 */
class CountStarredNamesInteractor(
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor
) : Interactor<Int, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<Int> {
        return getNamesWithStarsInteractor.buildFlow(null)
            .map { it.filter { name -> (name.stars ?: 0) > 0 }.count() }
    }
}