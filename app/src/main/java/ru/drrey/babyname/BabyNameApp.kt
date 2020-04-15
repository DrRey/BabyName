package ru.drrey.babyname

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.drrey.babyname.di.AppComponent
import ru.drrey.babyname.navigationmediator.NavigationMediatorComponent

class BabyNameApp : MultiDexApplication() {
    companion object {
        lateinit var instance: BabyNameApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        FirebaseApp.initializeApp(this)

        startKoin { androidLogger() }
        AppComponent.init()
        NavigationMediatorComponent.init()
    }
}