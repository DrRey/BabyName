package ru.drrey.babyname.welcome.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.navigation.AlphaNavigationFragment
import ru.drrey.babyname.welcome.di.WelcomeComponent
import ru.drrey.babyname.welcome.navigation.WelcomeFragmentScreen

class WelcomeFlowFragment : FlowFragment(), AlphaNavigationFragment {
    override val featureDependencies = listOf(WelcomeComponent::class)

    override fun getFlowScreen() = WelcomeFragmentScreen()
}