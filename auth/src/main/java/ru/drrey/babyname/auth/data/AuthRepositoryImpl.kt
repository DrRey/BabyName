package ru.drrey.babyname.auth.data

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import ru.drrey.babyname.auth.domain.entity.NotLoggedInException
import ru.drrey.babyname.auth.domain.repository.AuthRepository

const val PREFS_USER_ID = "prefs_user_id"

@ExperimentalCoroutinesApi
class AuthRepositoryImpl(private val sharedPreferences: SharedPreferences) : AuthRepository {
    override fun setUserId(userId: String): Flow<Nothing> = callbackFlow {
        sharedPreferences.edit().putString(PREFS_USER_ID, userId).apply()
        close()
        awaitClose { cancel() }
    }

    override fun getUserId(): Flow<String> = flow {
        val userId = sharedPreferences.getString(PREFS_USER_ID, null)
        userId?.let { emit(it) } ?: (throw NotLoggedInException())
    }
}