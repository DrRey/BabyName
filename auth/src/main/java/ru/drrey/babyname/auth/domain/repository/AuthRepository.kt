package ru.drrey.babyname.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun setUserId(userId: String): Flow<Nothing>
    fun getUserId(): Flow<String>
}