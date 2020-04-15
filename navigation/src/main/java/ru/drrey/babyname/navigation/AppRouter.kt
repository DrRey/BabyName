package ru.drrey.babyname.navigation

import ru.terrakok.cicerone.Router

class AppRouter(private val navigationMedator: NavigationMediator) : Router() {
    fun startFlow(flow: Flow) {
        if (flow is AppFlow) {
            navigateTo(navigationMedator.getScreen(flow))
        }
    }
}