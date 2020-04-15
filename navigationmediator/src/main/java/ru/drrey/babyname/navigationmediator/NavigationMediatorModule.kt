package ru.drrey.babyname.navigationmediator

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.drrey.babyname.navigation.NavigationMediator

object NavigationMediatorComponent {
    fun init() = loadKoinModules(
        navigationModule
    )

    private val navigationModule = module {
        single<NavigationMediator> { NavigationMediatorImpl(get()) }
    }
}