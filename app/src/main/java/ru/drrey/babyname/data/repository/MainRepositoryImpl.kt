package ru.drrey.babyname.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.drrey.babyname.domain.repository.MainRepository

const val PREFS_WELCOME_SCREEN = "prefs_welcome_screen"

class MainRepositoryImpl(private val sharedPreferences: SharedPreferences) : MainRepository {
    override fun checkWelcomeScreenShown(): Flow<Boolean> = flow {
        emit(sharedPreferences.getBoolean(PREFS_WELCOME_SCREEN, false))
    }
}