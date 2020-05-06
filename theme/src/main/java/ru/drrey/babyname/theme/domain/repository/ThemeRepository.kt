package ru.drrey.babyname.theme.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Theme repository
 */
interface ThemeRepository {
    fun savePrimaryColor(colorResId: Int): Flow<Unit>
    fun saveAccentColor(colorResId: Int): Flow<Unit>
    fun getPrimaryColor(): Flow<Int?>
    fun getAccentColor(): Flow<Int?>
}