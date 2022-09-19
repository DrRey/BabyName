package ru.drrey.babyname.names.navigation

import com.github.terrakok.cicerone.Screen
import ru.drrey.babyname.navigationmediator.FilterFlowScreenProvider

class FilterFlowScreenProviderImpl : FilterFlowScreenProvider {
    override fun getScreen(): Screen {
        return FilterScreen()
    }
}