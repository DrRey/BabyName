package ru.drrey.babyname.names.navigation

import ru.drrey.babyname.navigationmediator.FilterFlowScreenProvider
import ru.terrakok.cicerone.Screen

class FilterFlowScreenProviderImpl : FilterFlowScreenProvider {
    override fun getScreen(): Screen {
        return FilterScreen()
    }
}