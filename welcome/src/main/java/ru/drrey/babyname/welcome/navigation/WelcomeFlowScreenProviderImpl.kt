package ru.drrey.babyname.welcome.navigation

import ru.drrey.babyname.navigationmediator.WelcomeFlowScreenProvider
import ru.terrakok.cicerone.Screen

class WelcomeFlowScreenProviderImpl : WelcomeFlowScreenProvider {
    override fun getScreen(): Screen {
        return WelcomeScreen()
    }
}