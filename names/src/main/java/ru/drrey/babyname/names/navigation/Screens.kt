package ru.drrey.babyname.names.navigation

import ru.drrey.babyname.names.presentation.FilterFlowFragment
import ru.drrey.babyname.names.presentation.FilterFragment
import ru.drrey.babyname.names.presentation.NamesFlowFragment
import ru.drrey.babyname.names.presentation.NamesFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class NamesFragmentScreen : SupportAppScreen() {
    override fun getFragment() = NamesFragment.newInstance()
}

class NamesScreen : SupportAppScreen() {
    override fun getFragment() = NamesFlowFragment()
}

class FilterFragmentScreen : SupportAppScreen() {
    override fun getFragment() = FilterFragment.newInstance()
}

class FilterScreen : SupportAppScreen() {
    override fun getFragment() = FilterFlowFragment()
}