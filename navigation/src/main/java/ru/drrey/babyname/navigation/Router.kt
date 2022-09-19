package ru.drrey.babyname.navigation

import com.github.terrakok.cicerone.Router
import kotlin.reflect.KClass

class Router(private val navigationMediator: NavigationMediator) : Router() {

    /**
     * Open new flow and add it to the screens chain.
     *
     * @param flow flow
     */
    fun startFlow(flow: Flow) {
        navigateTo(navigationMediator.getScreen(flow))
    }

    /**
     * Prepares the feature to be used, but does not open its UI.
     *
     * @param featureApiClass feature api class
     */
    fun prepareFeature(featureApiClass: KClass<*>) {
        navigationMediator.prepareFeature(featureApiClass)
    }
}