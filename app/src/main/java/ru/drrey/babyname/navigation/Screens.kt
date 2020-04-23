package ru.drrey.babyname.navigation

import ru.drrey.babyname.presentation.MainFlowFragment
import ru.drrey.babyname.presentation.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainScreen : SupportAppScreen() {
    override fun getFragment() = MainFlowFragment()
}

class MainFragmentScreen : SupportAppScreen() {
    override fun getFragment() = MainFragment()
}