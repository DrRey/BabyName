package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names with stars interactor
 */
class GetNamesWithStarsInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>,
    private val getSexFilterInteractor: GetSexFilterInteractor
) : Interactor<List<Name>, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<List<Name>> {
        return getUserId().flatMapLatest { userId ->
            getSexFilterInteractor.buildFlow(null).flatMapLatest { sexFilter ->
                namesRepository.getNames()
                    .map { if (sexFilter == null) it else it.filter { name -> name.sex == sexFilter } }
                    .zip(namesRepository.getStars(userId)) { namesList, starsList ->
                        namesList.asSequence().map { name ->
                            name.apply {
                                stars =
                                    starsList.firstOrNull { star -> star.name == name.displayName }?.stars
                            }
                        }.sortedWith(compareBy({ it.sex }, { it.displayName })).toList()
                    }
            }
        }
    }
}