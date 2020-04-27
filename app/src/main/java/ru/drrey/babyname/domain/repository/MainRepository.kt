package ru.drrey.babyname.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Main repository
 */
interface MainRepository {
    fun checkWelcomeScreenShown(): Flow<Boolean>
}