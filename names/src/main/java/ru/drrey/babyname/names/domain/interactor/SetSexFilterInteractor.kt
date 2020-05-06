package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Set sex filter interactor
 */
class SetSexFilterInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Unit, Sex?>() {

    override fun buildFlow(params: Sex?): Flow<Unit> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.setSexFilter(userId, params)
        }
    }
}