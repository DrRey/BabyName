package ru.drrey.babyname.auth.data

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import ru.drrey.babyname.auth.domain.entity.NotLoggedInException
import ru.drrey.babyname.auth.domain.repository.AuthRepository

const val PREFS_USER_ID = "prefs_user_id"

class AuthRepositoryImpl(private val sharedPreferences: SharedPreferences) : AuthRepository {
    override fun setUserId(userId: String): Completable = Completable.fromRunnable {
        sharedPreferences.edit().putString(PREFS_USER_ID, userId).apply()
    }

    override fun getUserId(): Single<String> = Single.create<String> { single ->
        val userId = sharedPreferences.getString(PREFS_USER_ID, null)
        userId?.let { single.onSuccess(it) } ?: single.onError(NotLoggedInException())
    }
}