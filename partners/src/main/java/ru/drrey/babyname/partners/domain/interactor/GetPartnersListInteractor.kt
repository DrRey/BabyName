package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Get partners list interactor
 */
class GetPartnersListInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : BaseInteractor<List<Partner>, Void?>() {

    override fun buildFlow(params: Void?): Flow<List<Partner>> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.getPartnersList(userId)
        }
    }
}