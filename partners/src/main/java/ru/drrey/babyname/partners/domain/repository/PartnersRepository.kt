package ru.drrey.babyname.partners.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.partners.domain.entity.Partner

/**
 * Partners repository
 */
interface PartnersRepository {
    fun clearPartners(userId: String, partners: List<Partner>): Flow<Nothing>
    fun addPartner(userId: String, partnerId: String): Flow<Nothing>
    fun getPartnersList(userId: String): Flow<List<Partner>>
    fun getPartnersStars(partnerIds: List<String>): Flow<Map<String, List<NameStars>>>
}