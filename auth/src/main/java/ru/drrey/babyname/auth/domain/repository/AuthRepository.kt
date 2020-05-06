package ru.drrey.babyname.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun setUserId(userId: String): Flow<Unit>
    fun getUserId(): Flow<String>
}