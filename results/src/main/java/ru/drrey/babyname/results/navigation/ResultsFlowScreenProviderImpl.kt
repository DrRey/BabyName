package ru.drrey.babyname.results.navigation

import ru.drrey.babyname.navigationmediator.ResultsFlowScreenProvider
import com.github.terrakok.cicerone.Screen

class ResultsFlowScreenProviderImpl : ResultsFlowScreenProvider {
    override fun getScreen(): Screen {
        return ResultsScreen()
    }
}