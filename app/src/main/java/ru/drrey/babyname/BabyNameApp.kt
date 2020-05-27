package ru.drrey.babyname

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_OFF
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
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

        System.setProperty(
            DEBUG_PROPERTY_NAME,
            if (BuildConfig.DEBUG) DEBUG_PROPERTY_VALUE_ON else DEBUG_PROPERTY_VALUE_OFF
        )

        instance = this

        FirebaseApp.initializeApp(this)

        startKoin { androidLogger() }
        AppComponent.init()
        NavigationMediatorComponent.init()
    }
}