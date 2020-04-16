package ru.drrey.babyname.di

import android.content.Context
import android.preference.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import ru.drrey.babyname.BabyNameApp
import ru.drrey.babyname.FeatureStarter
import ru.drrey.babyname.navigation.AppRouter
import ru.drrey.babyname.navigationmediator.FeatureProvider
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

object AppComponent {
    fun init() = loadKoinModules(
        listOf(
            appModule
        )
    )

    private val appModule = module {
        single<Context> { BabyNameApp.instance.applicationContext }
        single<Cicerone<*>> { Cicerone.create(AppRouter(get())) }
        single<Router> { get<Cicerone<*>>().router as AppRouter }
        single { get<Cicerone<*>>().navigatorHolder }
        single { FirebaseFirestore.getInstance() }
        single { PreferenceManager.getDefaultSharedPreferences(get()) }

        single<FeatureProvider> { FeatureStarter }
    }
}
