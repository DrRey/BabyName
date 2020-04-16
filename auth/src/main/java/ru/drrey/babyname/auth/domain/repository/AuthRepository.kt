package ru.drrey.babyname.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun setUserId(userId: String): Flow<Void>
    fun getUserId(): Flow<String>
}