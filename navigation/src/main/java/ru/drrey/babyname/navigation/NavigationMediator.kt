package ru.drrey.babyname.navigation

import ru.terrakok.cicerone.Screen
import kotlin.reflect.KClass

interface NavigationMediator {
    fun getScreen(flow: Flow): Screen
    fun prepareFeature(featureApiClass: KClass<*>)
}