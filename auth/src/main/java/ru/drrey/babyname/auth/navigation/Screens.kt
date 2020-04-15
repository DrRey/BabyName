package ru.drrey.babyname.auth.navigation

import android.content.Context
import android.content.Intent
import ru.drrey.babyname.auth.presentation.AuthActivity
import ru.drrey.babyname.common.navigation.AppScreen

class AuthScreen : AppScreen() {
    override fun getActivityIntent(context: Context): Intent {
        return Intent(context, AuthActivity::class.java)
    }
}