package ru.drrey.babyname.auth.navigation

import ru.drrey.babyname.auth.presentation.AuthFlowFragment
import ru.drrey.babyname.auth.presentation.AuthFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AuthScreen : SupportAppScreen() {
    override fun getFragment() = AuthFlowFragment()
}

class AuthFragmentScreen : SupportAppScreen() {
    override fun getFragment() = AuthFragment.newInstance()
}