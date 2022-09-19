package ru.drrey.babyname.welcome.navigation

import androidx.fragment.app.FragmentFactory
import ru.drrey.babyname.welcome.presentation.WelcomeFlowFragment
import ru.drrey.babyname.welcome.presentation.WelcomeFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class WelcomeScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = WelcomeFlowFragment()
}

class WelcomeFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = WelcomeFragment()
}