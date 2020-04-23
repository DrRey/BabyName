package ru.drrey.babyname.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.di.MainComponent
import ru.drrey.babyname.navigation.MainFragmentScreen

class MainFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(MainComponent::class)

    override fun getFlowScreen() = MainFragmentScreen()
}