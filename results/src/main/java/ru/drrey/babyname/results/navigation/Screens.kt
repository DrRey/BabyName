package ru.drrey.babyname.results.navigation

import ru.drrey.babyname.results.presentation.ResultsFlowFragment
import ru.drrey.babyname.results.presentation.ResultsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ResultsFragmentScreen : SupportAppScreen() {
    override fun getFragment() = ResultsFragment.newInstance()
}

class ResultsScreen : SupportAppScreen() {
    override fun getFragment() = ResultsFlowFragment()
}