package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names interactor
 */
class GetNamesInteractor(
    private val namesRepository: NamesRepository
) : Interactor<List<Name>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<Name>> {
        return namesRepository.getNames()
            .map { it.sortedWith(compareBy({ name -> name.sex }, { name -> name.displayName })) }
    }
}