package ru.drrey.babyname.names.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.names.di.NamesComponent
import ru.drrey.babyname.names.navigation.NamesFragmentScreen

class NamesFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(NamesComponent::class)

    override fun getFlowScreen() = NamesFragmentScreen()
}