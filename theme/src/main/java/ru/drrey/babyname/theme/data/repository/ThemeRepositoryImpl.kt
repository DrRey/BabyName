package ru.drrey.babyname.theme.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.drrey.babyname.theme.domain.repository.ThemeRepository

const val PREFS_COLOR_PRIMARY = "prefs_color_primary"
const val PREFS_COLOR_ACCENT = "prefs_color_accent"

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {

    override fun savePrimaryColor(colorResId: Int): Flow<Unit> = flow {
        sharedPreferences.edit().putInt(PREFS_COLOR_PRIMARY, colorResId).apply()
        emit(Unit)
    }

    override fun saveAccentColor(colorResId: Int): Flow<Unit> = flow {
        sharedPreferences.edit().putInt(PREFS_COLOR_ACCENT, colorResId).apply()
        emit(Unit)
    }

    override fun getPrimaryColor(): Flow<Int?> = flow {
        emit(sharedPreferences.getInt(PREFS_COLOR_PRIMARY, -1).takeIf { it > 0 })
    }

    override fun getAccentColor(): Flow<Int?> = flow {
        emit(sharedPreferences.getInt(PREFS_COLOR_ACCENT, -1).takeIf { it > 0 })
    }
}