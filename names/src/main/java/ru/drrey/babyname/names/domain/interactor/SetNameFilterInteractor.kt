package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Set name filter interactor
 */
class SetNameFilterInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Unit, SetNameFilterInteractor.Params>() {

    override fun buildFlow(params: Params): Flow<Unit> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.setNameFilter(userId, params.name, params.allow)
        }
    }

    class Params(val name: Name, val allow: Boolean)
}