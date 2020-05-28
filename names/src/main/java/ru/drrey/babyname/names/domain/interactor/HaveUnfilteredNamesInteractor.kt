package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor

/**
 * Count unfiltered names interactor
 */
class CountUnfilteredNamesInteractor(
    private val getUnfilteredNamesInteractor: GetUnfilteredNamesInteractor
) : Interactor<Int, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<Int> {
        return getUnfilteredNamesInteractor.buildFlow(null).map { it.count() }
    }
}