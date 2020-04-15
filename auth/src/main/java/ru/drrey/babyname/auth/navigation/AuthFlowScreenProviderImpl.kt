package ru.drrey.babyname.auth.navigation

import ru.drrey.babyname.navigationmediator.AuthFlowScreenProvider
import ru.terrakok.cicerone.Screen

class AuthFlowScreenProviderImpl : AuthFlowScreenProvider {
    override fun getScreen(): Screen {
        return AuthScreen()
    }
}