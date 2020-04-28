package ru.drrey.babyname.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.drrey.babyname.domain.repository.MainRepository

const val PREFS_FIRST_START = "prefs_first_start"

class MainRepositoryImpl(private val sharedPreferences: SharedPreferences) : MainRepository {
    override fun checkFirstStart(): Flow<Boolean> = flow {
        emit(sharedPreferences.getBoolean(PREFS_FIRST_START, false).also {
            sharedPreferences.edit().putBoolean(PREFS_FIRST_START, true).apply()
        })
    }
}