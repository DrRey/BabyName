package ru.drrey.babyname.partners.api

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.navigationmediator.AddPartnerFlowScreenProvider
import ru.drrey.babyname.navigationmediator.PartnersQrCodeFlowScreenProvider

interface PartnersApi {
    fun getPartnersQrCodeFlowScreenProvider(): PartnersQrCodeFlowScreenProvider
    fun getAddPartnersFlowScreenProvider(): AddPartnerFlowScreenProvider
    fun clearPartnersInteractor(): Interactor<Unit, Nothing?>
    fun getPartnerIdsListInteractor(): Interactor<List<String>, Nothing?>
    fun getPartnerIds(userId: String): Flow<List<String>>
    fun getPartnersStars(partnerIds: List<String>): Flow<Pair<String, List<NameStars>>>
}