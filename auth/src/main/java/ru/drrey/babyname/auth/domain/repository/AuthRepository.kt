package ru.drrey.babyname.auth.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {
    fun setUserId(userId: String): Completable
    fun getUserId(): Single<String>
}