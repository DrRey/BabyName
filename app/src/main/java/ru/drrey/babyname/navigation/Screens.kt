package ru.drrey.babyname.navigation

import androidx.fragment.app.FragmentFactory
import ru.drrey.babyname.presentation.MainFlowFragment
import ru.drrey.babyname.presentation.MainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class MainScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = MainFlowFragment()
}

class MainFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = MainFragment()
}