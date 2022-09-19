package ru.drrey.babyname.results.navigation

import androidx.fragment.app.FragmentFactory
import ru.drrey.babyname.results.presentation.ResultsFlowFragment
import ru.drrey.babyname.results.presentation.ResultsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class ResultsFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = ResultsFragment.newInstance()
}

class ResultsScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = ResultsFlowFragment()
}