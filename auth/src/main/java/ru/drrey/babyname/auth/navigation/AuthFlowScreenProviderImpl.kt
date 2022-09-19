package ru.drrey.babyname.auth.navigation

import com.github.terrakok.cicerone.Screen
import ru.drrey.babyname.navigationmediator.AuthFlowScreenProvider

class AuthFlowScreenProviderImpl : AuthFlowScreenProvider {
    override fun getScreen(): Screen {
        return AuthScreen()
    }
}