package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Clear partners interactor
 */
@ExperimentalCoroutinesApi
class ClearPartnersInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : BaseInteractor<Nothing, Void?>() {

    override fun buildFlow(params: Void?): Flow<Nothing> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.getPartnersList(userId).flatMapLatest { partnerIds ->
                partnersRepository.clearPartners(userId, partnerIds)
            }
        }
    }
}