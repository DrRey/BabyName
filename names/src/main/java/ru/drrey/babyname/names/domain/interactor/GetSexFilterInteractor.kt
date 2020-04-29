package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Get sex filter interactor
 */
class GetSexFilterInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Sex?, Void?>() {

    override fun buildFlow(params: Void?): Flow<Sex?> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.getSexFilter(userId)
        }
    }
}