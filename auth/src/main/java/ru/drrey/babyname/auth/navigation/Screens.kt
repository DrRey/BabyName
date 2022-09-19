package ru.drrey.babyname.auth.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.drrey.babyname.auth.presentation.AuthFlowFragment
import ru.drrey.babyname.auth.presentation.AuthFragment

class AuthScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = AuthFlowFragment()
}

class AuthFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = AuthFragment.newInstance()
}