package ru.drrey.babyname.names.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

class NamesRepositoryImpl(private val db: FirebaseFirestore) : NamesRepository {
    override fun getNames(): Flow<List<Name>> = callbackFlow {
        db.collection("names").get()
            .addOnSuccessListener { names ->
                try {
                    if (isActive) {
                        trySend(names.toObjects(Name::class.java)
                            .filter { (it.stars ?: 0) >= 0 })
                    }
                    close()
                } catch (e: Exception) {
                    close(e)
                }
            }.addOnFailureListener {
                close(it)
            }
        awaitClose()
    }

    override fun getStars(userId: String): Flow<List<NameStars>> = callbackFlow {
        db.collection(userId).get()
            .addOnSuccessListener { stars ->
                try {
                    if (isActive) {
                        trySend(stars.toObjects(NameStars::class.java).toList()).isSuccess
                    }
                    close()
                } catch (e: Exception) {
                    close(e)
                }
            }.addOnFailureListener {
                close(it)
            }
        awaitClose()
    }

    override fun setStars(userId: String, name: Name, stars: Int): Flow<Unit> = callbackFlow {
        db.collection(userId).document(name.displayName).set(
            NameStars(
                name.displayName,
                stars
            )
        ).addOnCompleteListener {
            if (isActive) {
                trySend(Unit).isSuccess
            }
            close()
        }.addOnFailureListener {
            close(it)
        }
        awaitClose()
    }

    override fun getSexFilter(userId: String): Flow<Sex?> = callbackFlow {
        db.collection("filters_$userId").document("sex").get()
            .addOnSuccessListener { doc ->
                if (isActive) {
                    trySend(doc.get("sex")
                        ?.takeIf { it.toString().isNotEmpty() }
                        ?.let { Sex.valueOf(it.toString()) }
                    ).isSuccess
                }
                close()
            }.addOnFailureListener {
                close(it)
            }
        awaitClose()
    }

    override fun setSexFilter(userId: String, sex: Sex?): Flow<Unit> = callbackFlow {
        db.collection("filters_$userId").document("sex").set(
            mapOf(Pair("sex", sex?.toString() ?: ""))
        ).addOnCompleteListener {
            if (isActive) {
                trySend(Unit).isSuccess
            }
            close()
        }.addOnFailureListener {
            close(it)
        }
        awaitClose()
    }

    override fun setNameFilter(userId: String, name: Name, allow: Boolean): Flow<Unit> =
        callbackFlow {
            db.collection(userId).document(name.displayName).set(
                NameStars(
                    name.displayName,
                    if (allow) 0 else -1
                )
            ).addOnCompleteListener {
                if (isActive) {
                    trySend(Unit).isSuccess
                }
                close()
            }.addOnFailureListener {
                close(it)
            }
            awaitClose()
        }
}