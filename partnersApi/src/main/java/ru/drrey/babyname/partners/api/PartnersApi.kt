package ru.drrey.babyname.partners.api

import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.navigationmediator.AddPartnerFlowScreenProvider
import ru.drrey.babyname.navigationmediator.PartnersQrCodeFlowScreenProvider

interface PartnersApi {
    fun getPartnersQrCodeFlowScreenProvider(): PartnersQrCodeFlowScreenProvider
    fun getAddPartnersFlowScreenProvider(): AddPartnerFlowScreenProvider
    fun clearPartnersInteractor(): BaseInteractor<Void, Void?>
    fun getPartnerIdsListInteractor(): BaseInteractor<List<String>, Void?>
    fun getPartnerIds(userId: String): Single<List<String>>
    fun getPartnersStars(partnerIds: List<String>): Single<Map<String, List<NameStars>>>
}