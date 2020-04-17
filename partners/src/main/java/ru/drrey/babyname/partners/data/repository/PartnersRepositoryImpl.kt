package ru.drrey.babyname.partners.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

@ExperimentalCoroutinesApi
class PartnersRepositoryImpl(private val db: FirebaseFirestore) : PartnersRepository {
    override fun clearPartners(userId: String, partners: List<Partner>): Flow<Nothing> =
        partners.map { partner ->
            callbackFlow<Nothing> {
                db.collection("partners_$userId").document(partner.id).delete()
                    .addOnSuccessListener {
                        close()
                    }
                    .addOnFailureListener { exception ->
                        close(exception)
                    }
                awaitClose { cancel() }
            }.flatMapLatest {
                callbackFlow<Nothing> {
                    db.collection("partners_${partner.id}").document(userId).delete()
                        .addOnSuccessListener {
                            close()
                        }
                        .addOnFailureListener { exception ->
                            close(exception)
                        }
                    awaitClose { cancel() }
                }
            }
        }.merge()

    override fun addPartner(userId: String, partnerId: String): Flow<Nothing> =
        callbackFlow<Nothing> {
            db.collection("partners_$userId").document(partnerId).set(Partner(partnerId))
                .addOnSuccessListener {
                    close()
                }
                .addOnFailureListener { exception ->
                    close(exception)
                }
            awaitClose { cancel() }
        }.flatMapLatest {
            callbackFlow<Nothing> {
                db.collection("partners_$partnerId").document(userId).set(Partner(userId))
                    .addOnSuccessListener {
                        close()
                    }
                    .addOnFailureListener { exception ->
                        close(exception)
                    }
                awaitClose { cancel() }
            }
        }

    override fun getPartnersList(userId: String): Flow<List<Partner>> = callbackFlow {
        db.collection("partners_$userId").get()
            .addOnSuccessListener { partners ->
                try {
                    offer(partners.toObjects(Partner::class.java).toList())
                } catch (t: Throwable) {
                    close(t)
                }
            }
            .addOnFailureListener {
                offer(emptyList())
            }
        awaitClose { cancel() }
    }

    override fun getPartnersStars(partnerIds: List<String>): Flow<Map<String, List<NameStars>>> =
        callbackFlow {
            partnerIds.map { partnerId ->
                callbackFlow<Pair<String, List<NameStars>>> {
                    db.collection(partnerId).get()
                        .addOnSuccessListener { stars ->
                            try {
                                offer(
                                    Pair(
                                        partnerId,
                                        stars.toObjects(NameStars::class.java).toList()
                                    )
                                )
                            } catch (t: Throwable) {
                                close(t)
                            }
                        }
                        .addOnFailureListener {
                            offer(emptyMap())
                        }
                    awaitClose { cancel() }
                }
            }.merge().toList().run {
                offer(associateBy({ it.first }, { it.second }))
            }
            awaitClose { cancel() }
        }
}