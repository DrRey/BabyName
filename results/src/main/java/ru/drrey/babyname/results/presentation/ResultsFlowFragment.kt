package ru.drrey.babyname.results.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.results.ResultsComponent
import ru.drrey.babyname.results.navigation.ResultsFragmentScreen

class ResultsFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(ResultsComponent::class)

    override fun getFlowScreen() = ResultsFragmentScreen()
}