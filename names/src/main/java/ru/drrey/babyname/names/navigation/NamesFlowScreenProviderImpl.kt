package ru.drrey.babyname.names.navigation

import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider
import com.github.terrakok.cicerone.Screen

class NamesFlowScreenProviderImpl : NamesFlowScreenProvider {
    override fun getScreen(): Screen {
        return NamesScreen()
    }
}