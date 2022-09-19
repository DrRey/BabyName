package ru.drrey.babyname.di

import android.preference.PreferenceManager
import com.github.terrakok.cicerone.Cicerone
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.drrey.babyname.BabyNameApp
import ru.drrey.babyname.FeatureStarter
import ru.drrey.babyname.navigation.LocalCiceroneHolder
import ru.drrey.babyname.navigation.Router
import ru.drrey.babyname.navigationmediator.FeatureProvider

object AppComponent {
    fun init() = loadKoinModules(
        listOf(
            appModule
        )
    )

    private val appModule = module {
        single { BabyNameApp.instance.applicationContext }
        single { FirebaseFirestore.getInstance() }
        single { PreferenceManager.getDefaultSharedPreferences(get()) }
        single<FeatureProvider> { FeatureStarter }
        factory { (containerTag: String) -> LocalCiceroneHolder.getCicerone(containerTag) }
        factory { get<Cicerone<Router>> { parametersOf("") }.router }
    }
}
