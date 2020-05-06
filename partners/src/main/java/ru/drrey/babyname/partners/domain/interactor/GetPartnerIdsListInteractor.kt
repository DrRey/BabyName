package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Get partner ids list interactor
 */
class GetPartnerIdsListInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<List<String>, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<List<String>> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.getPartnersList(userId).map { it.map { partner -> partner.id } }
        }
    }
}