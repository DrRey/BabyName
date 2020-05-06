package ru.drrey.babyname.partners.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

class PartnersRepositoryImpl(private val db: FirebaseFirestore) : PartnersRepository {
    override fun clearPartners(userId: String, partners: List<Partner>): Flow<Unit> =
        partners.map { partner ->
            callbackFlow {
                db.collection("partners_$userId").document(partner.id).delete()
                    .addOnSuccessListener {
                        offer(Unit)
                        close()
                    }
                    .addOnFailureListener { exception ->
                        close(exception)
                    }
                awaitClose { cancel() }
            }.flatMapLatest {
                callbackFlow {
                    db.collection("partners_${partner.id}").document(userId).delete()
                        .addOnSuccessListener {
                            offer(Unit)
                            close()
                        }
                        .addOnFailureListener { exception ->
                            close(exception)
                        }
                    awaitClose { cancel() }
                }
            }
        }.merge()

    override fun addPartner(userId: String, partnerId: String): Flow<Unit> =
        callbackFlow {
            db.collection("partners_$userId").document(partnerId).set(Partner(partnerId))
                .addOnSuccessListener {
                    offer(Unit)
                    close()
                }
                .addOnFailureListener { exception ->
                    close(exception)
                }
            awaitClose { cancel() }
        }.flatMapLatest {
            callbackFlow {
                db.collection("partners_$partnerId").document(userId).set(Partner(userId))
                    .addOnSuccessListener {
                        offer(Unit)
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
                    close()
                } catch (t: Throwable) {
                    close(t)
                }
            }
            .addOnFailureListener {
                offer(emptyList())
                close()
            }
        awaitClose { cancel() }
    }

    override fun getPartnersStars(partnerIds: List<String>): Flow<Pair<String, List<NameStars>>> =
        partnerIds.map { partnerId ->
            callbackFlow<Pair<String, List<NameStars>>> {
                db.collection(partnerId).get()
                    .addOnSuccessListener { stars ->
                        try {
                            offer(Pair(partnerId, stars.toObjects(NameStars::class.java).toList()))
                            close()
                        } catch (t: Throwable) {
                            close(t)
                        }
                    }
                    .addOnFailureListener {
                        close(it)
                    }
                awaitClose { cancel() }
            }
        }.merge()
}