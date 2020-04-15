package ru.drrey.babyname.common.di

import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class FeatureModuleProvider {
    open val modules: List<Module> = emptyList()
}

abstract class FeatureCoreComponent : FeatureModuleProvider()

interface FeatureDependencies : KoinComponent

abstract class FeatureComponent<T : FeatureDependencies> : KoinComponent, FeatureModuleProvider() {
    private var isInit = false

    fun load(): FeatureComponent<T>? {
        var needsInit = false
        if (!isInit) {
            loadModules()
            needsInit = true
        }
        return if (needsInit) this else null
    }


    open fun init(dependencies: T) {
        isInit = true
    }

    open fun reset() {
        if (isInit) {
            unloadModules()
        }
    }

    private fun loadModules() {
        modules.run {
            if (isNotEmpty()) {
                loadKoinModules(this)
            }
        }
    }

    private fun unloadModules() {
        modules.run {
            if (isNotEmpty()) {
                unloadKoinModules(this)
            }
        }
        isInit = false
    }
}