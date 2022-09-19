package ru.drrey.babyname.welcome.navigation

import ru.drrey.babyname.navigationmediator.WelcomeFlowScreenProvider
import com.github.terrakok.cicerone.Screen

class WelcomeFlowScreenProviderImpl : WelcomeFlowScreenProvider {
    override fun getScreen(): Screen {
        return WelcomeScreen()
    }
}