package ru.drrey.babyname.partners.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.partners.domain.entity.Partner

/**
 * Partners repository
 */
interface PartnersRepository {
    fun clearPartners(userId: String, partners: List<Partner>): Completable
    fun addPartner(userId: String, partnerId: String): Completable
    fun getPartnersList(userId: String): Single<List<Partner>>
    fun getPartnersStars(partnerIds: List<String>): Single<Map<String, List<NameStars>>>
}