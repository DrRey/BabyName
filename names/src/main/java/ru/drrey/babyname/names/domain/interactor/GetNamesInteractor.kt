package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names interactor
 */
class GetNamesInteractor(
    private val namesRepository: NamesRepository,
    private val getSexFilterInteractor: GetSexFilterInteractor
) : Interactor<List<Name>, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<List<Name>> {
        return getSexFilterInteractor.buildFlow(null).flatMapLatest { sexFilter ->
            namesRepository.getNames()
                .map {
                    it.filter { name -> sexFilter == null || name.sex == sexFilter }.sortedWith(
                        compareBy(
                            { name -> name.sex },
                            { name -> name.displayName })
                    )
                }
        }
    }
}