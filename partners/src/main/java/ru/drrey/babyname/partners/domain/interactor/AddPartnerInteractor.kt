package ru.drrey.babyname.partners.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

/**
 * Add partner interactor
 */
class AddPartnerInteractor(
    private val partnersRepository: PartnersRepository,
    private val getUserId: () -> Flow<String>
) : Interactor<Void?, String>() {

    override fun buildFlow(params: String): Flow<Void?> {
        return getUserId().flatMapLatest { userId ->
            partnersRepository.addPartner(userId, params)
        }
    }
}