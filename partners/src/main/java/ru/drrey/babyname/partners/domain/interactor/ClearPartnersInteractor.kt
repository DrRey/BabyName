package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Clear partners interactor
 */
class ClearPartnersInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Unit, Nothing?>() {

    override fun buildFlow(params: Nothing?): Flow<Unit> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.getPartnersList(userId).flatMapLatest { partnerIds ->
                partnersRepository.clearPartners(userId, partnerIds)
            }
        }
    }
}