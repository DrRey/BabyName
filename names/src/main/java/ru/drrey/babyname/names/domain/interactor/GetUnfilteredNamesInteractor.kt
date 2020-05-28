package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name

/**
 * Get unfiltered names interactor
 */
class GetUnfilteredNamesInteractor(
    private val getNamesWithStarsInteractor: GetNamesWithStarsInteractor
) : Interactor<List<Name>, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<List<Name>> {
        return getNamesWithStarsInteractor.buildFlow(null)
            .map { it.filter { name -> name.stars == null } }
    }
}