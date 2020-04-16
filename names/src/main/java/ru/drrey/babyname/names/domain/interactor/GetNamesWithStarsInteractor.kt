package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.zip
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get names with stars interactor
 */
@ExperimentalCoroutinesApi
class GetNamesWithStarsInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : BaseInteractor<List<Name>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<Name>> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.getNames()
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