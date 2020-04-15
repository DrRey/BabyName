package ru.drrey.babyname.names.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

class NamesRepositoryImpl(private val db: FirebaseFirestore) : NamesRepository {
    override fun getNames(): Single<List<Name>> = Single.create { singleEmitter ->
        db.collection("names").get()
            .addOnSuccessListener { names ->
                try {
                    names.toObjects(Name::class.java).let {
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

    override fun getStars(userId: String): Single<List<NameStars>> =
        Single.create { singleEmitter ->
            db.collection(userId).get()
                .addOnSuccessListener { stars ->
                    try {
                        stars.toObjects(NameStars::class.java).let {
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

    override fun setStars(userId: String, name: Name, stars: Int): Completable =
        Completable.create { completableEmitter ->
            db.collection(userId).document(name.displayName).set(
                NameStars(
                    name.displayName,
                    stars
                )
            )
                .addOnCompleteListener {
                    completableEmitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    completableEmitter.tryOnError(exception)
                }
        }

    override fun getSexFilter(userId: String): Single<String> = Single.create { singleEmitter ->
        db.collection("filters_$userId").document("sex").get()
            .addOnSuccessListener { doc ->
                try {
                    singleEmitter.onSuccess((doc.get("sex") ?: "ALL").toString())
                } catch (t: Throwable) {
                    singleEmitter.tryOnError(t)
                }
            }
            .addOnFailureListener { exception ->
                singleEmitter.tryOnError(exception)
            }
    }

    override fun setSexFilter(userId: String, sex: String): Completable =
        Completable.create { completableEmitter ->
            db.collection("filters_$userId").document("sex").set(
                sex
            )
                .addOnCompleteListener {
                    completableEmitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    completableEmitter.tryOnError(exception)
                }
        }
}