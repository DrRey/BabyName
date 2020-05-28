package ru.drrey.babyname.names.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.names.di.NamesComponent
import ru.drrey.babyname.names.navigation.FilterFragmentScreen

class FilterFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(NamesComponent::class)

    override fun getFlowScreen() = FilterFragmentScreen()
}