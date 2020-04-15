package ru.drrey.babyname.partners.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.partners.domain.entity.Partner
import ru.drrey.babyname.partners.domain.repository.PartnersRepository

class PartnersRepositoryImpl(private val db: FirebaseFirestore) : PartnersRepository {
    override fun clearPartners(userId: String, partners: List<Partner>): Completable {
        val completableList = partners.map { partner ->
            Completable.create { completableEmitter ->
                db.collection("partners_$userId").document(partner.id).delete()
                    .addOnSuccessListener {
                        completableEmitter.onComplete()
                    }
                    .addOnFailureListener { exception ->
                        completableEmitter.onError(exception)
                    }
            }.mergeWith(Completable.create { completableEmitter ->
                db.collection("partners_${partner.id}").document(userId).delete()
                    .addOnSuccessListener {
                        completableEmitter.onComplete()
                    }
                    .addOnFailureListener { exception ->
                        completableEmitter.onError(exception)
                    }
            })
        }
        return Completable.mergeDelayError(completableList)
    }

    override fun addPartner(userId: String, partnerId: String) =
        Completable.create { completableEmitter ->
            db.collection("partners_$userId").document(partnerId).set(Partner(partnerId))
                .addOnSuccessListener {
                    completableEmitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    completableEmitter.tryOnError(exception)
                }
        }.mergeWith(Completable.create { completableEmitter ->
            db.collection("partners_$partnerId").document(userId).set(Partner(userId))
                .addOnSuccessListener {
                    completableEmitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    completableEmitter.tryOnError(exception)
                }
        })

    override fun getPartnersList(userId: String): Single<List<Partner>> =
        Single.create { singleEmitter ->
            db.collection("partners_$userId").get()
                .addOnSuccessListener { partners ->
                    try {
                        partners.toObjects(Partner::class.java).let {
                            singleEmitter.onSuccess(it.toList())
                        }
                    } catch (t: Throwable) {
                        singleEmitter.tryOnError(t)
                    }
                }
                .addOnFailureListener { exception ->
                    singleEmitter.tryOnError(exception)
                }
        }

    @Suppress("UNCHECKED_CAST")
    override fun getPartnersStars(partnerIds: List<String>): Single<Map<String, List<NameStars>>> {
        val singlesList: MutableList<Single<Pair<String, List<NameStars>>>> = mutableListOf()
        partnerIds.forEach { partnerId ->
            singlesList.add(Single.create<Pair<String, List<NameStars>>> { singleEmitter ->
                db.collection(partnerId).get()
                    .addOnSuccessListener { stars ->
                        try {
                            stars.toObjects(NameStars::class.java).let {
                                singleEmitter.onSuccess(Pair(partnerId, it.toList()))
                            }
                        } catch (t: Throwable) {
                            singleEmitter.tryOnError(t)
                        }
                    }
                    .addOnFailureListener { exception ->
                        singleEmitter.tryOnError(exception)
                    }
            })
        }
        return Single.zip(singlesList) { anyPairs ->
            anyPairs.map { it as Pair<String, List<NameStars>> }.let { starPairs ->
                starPairs.associateBy({ it.first }, { it.second })
            }
        }
    }
}