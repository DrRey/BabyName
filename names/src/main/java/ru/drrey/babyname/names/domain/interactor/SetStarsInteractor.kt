package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Set stars interactor
 */
class SetStarsInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Unit, SetStarsInteractor.Params>() {

    override fun buildFlow(params: Params): Flow<Unit> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.setStars(userId, params.name, params.stars)
        }
    }

    class Params(val name: Name, val stars: Int)
}