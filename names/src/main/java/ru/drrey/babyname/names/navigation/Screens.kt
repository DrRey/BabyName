package ru.drrey.babyname.names.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.drrey.babyname.names.presentation.FilterFlowFragment
import ru.drrey.babyname.names.presentation.FilterFragment
import ru.drrey.babyname.names.presentation.NamesFlowFragment
import ru.drrey.babyname.names.presentation.NamesFragment

class NamesFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = NamesFragment.newInstance()
}

class NamesScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = NamesFlowFragment()
}

class FilterFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = FilterFragment.newInstance()
}

class FilterScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = FilterFlowFragment()
}