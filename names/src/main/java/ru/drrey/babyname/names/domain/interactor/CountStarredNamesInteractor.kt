package ru.drrey.babyname.names.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.domain.repository.NamesRepository

/**
 * Count names with stars interactor
 */
class CountStarredNamesInteractor(
    private val namesRepository: NamesRepository,
    private val getUserId: () -> Flow<String>
) : BaseInteractor<Int, Void?>() {

    override fun buildFlow(params: Void?): Flow<Int> {
        return getUserId().flatMapLatest { userId ->
            namesRepository.getStars(userId).map { it.size }
        }
    }
}