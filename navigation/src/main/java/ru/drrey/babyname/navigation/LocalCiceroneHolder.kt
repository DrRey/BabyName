package ru.drrey.babyname.navigation

import org.koin.core.KoinComponent
import org.koin.core.get
import ru.terrakok.cicerone.Cicerone
import java.util.*

object LocalCiceroneHolder : KoinComponent {
    private val containers: HashMap<String, Cicerone<Router>> = HashMap()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            containers[containerTag] = Cicerone.create(Router(get()))
        }
        return containers[containerTag]!!
    }
}