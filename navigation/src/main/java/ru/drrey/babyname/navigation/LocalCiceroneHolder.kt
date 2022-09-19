package ru.drrey.babyname.navigation

import com.github.terrakok.cicerone.Cicerone
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object LocalCiceroneHolder : KoinComponent {
    private val containers: HashMap<String, Cicerone<Router>> = HashMap()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            containers[containerTag] = Cicerone.create(Router(get()))
        }
        return containers[containerTag]!!
    }
}