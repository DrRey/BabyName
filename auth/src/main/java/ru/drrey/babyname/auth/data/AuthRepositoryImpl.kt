package ru.drrey.babyname.auth.data

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import ru.drrey.babyname.auth.api.NotLoggedInException
import ru.drrey.babyname.auth.domain.repository.AuthRepository

const val PREFS_USER_ID = "prefs_user_id"

class AuthRepositoryImpl(private val sharedPreferences: SharedPreferences) : AuthRepository {
    override fun setUserId(userId: String): Flow<Unit> = callbackFlow {
        sharedPreferences.edit().putString(PREFS_USER_ID, userId).apply()
        offer(Unit)
        close()
        awaitClose()
    }

    override fun getUserId(): Flow<String> = flow {
        val userId = sharedPreferences.getString(PREFS_USER_ID, null)
        userId?.let { emit(it) } ?: (throw NotLoggedInException())
    }
}