package ru.drrey.babyname.names.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.drrey.babyname.common.domain.entity.NameStars
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.names.domain.repository.NamesRepository

class NamesRepositoryImpl(private val db: FirebaseFirestore) : NamesRepository {
    override fun getNames(): Flow<List<Name>> = callbackFlow {
        db.collection("names").get()
            .addOnSuccessListener { names ->
                try {
                    offer(names.toObjects(Name::class.java).toList())
                } catch (e: Exception) {
                    close(e)
                }
            }.addOnFailureListener {
                close(it)
            }
        awaitClose { cancel() }
    }

    override fun getStars(userId: String): Flow<List<NameStars>> = callbackFlow {
        db.collection(userId).get()
            .addOnSuccessListener { stars ->
                try {
                    offer(stars.toObjects(NameStars::class.java).toList())
                } catch (e: Exception) {
                    close(e)
                }
            }.addOnFailureListener {
                close(it)
            }
        awaitClose { cancel() }
    }

    override fun setStars(userId: String, name: Name, stars: Int): Flow<Nothing> = callbackFlow {
        db.collection(userId).document(name.displayName).set(
            NameStars(
                name.displayName,
                stars
            )
        ).addOnCompleteListener {
            close()
        }.addOnFailureListener {
            close(it)
        }
        awaitClose { cancel() }
    }

    override fun getSexFilter(userId: String): Flow<String> = callbackFlow {
        db.collection("filters_$userId").document("sex").get()
            .addOnSuccessListener { doc ->
                offer((doc.get("sex") ?: "ALL").toString())
            }.addOnFailureListener {
                close(it)
            }
        awaitClose { cancel() }
    }

    override fun setSexFilter(userId: String, sex: String): Flow<Nothing> = callbackFlow {
        db.collection("filters_$userId").document("sex").set(
            sex
        ).addOnCompleteListener {
            close()
        }.addOnFailureListener {
            close(it)
        }
    }
}