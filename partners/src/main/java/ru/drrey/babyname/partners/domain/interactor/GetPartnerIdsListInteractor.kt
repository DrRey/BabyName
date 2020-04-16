package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Get partner ids list interactor
 */
@ExperimentalCoroutinesApi
class GetPartnerIdsListInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : BaseInteractor<List<String>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<String>> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.getPartnersList(userId).map { it.map { partner -> partner.id } }
        }
    }
}