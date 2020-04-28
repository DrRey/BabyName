package ru.drrey.babyname.welcome.navigation

import ru.drrey.babyname.welcome.presentation.WelcomeFlowFragment
import ru.drrey.babyname.welcome.presentation.WelcomeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class WelcomeScreen : SupportAppScreen() {
    override fun getFragment() = WelcomeFlowFragment()
}

class WelcomeFragmentScreen : SupportAppScreen() {
    override fun getFragment() = WelcomeFragment()
}