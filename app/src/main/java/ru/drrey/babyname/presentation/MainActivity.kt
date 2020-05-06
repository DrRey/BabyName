package ru.drrey.babyname.presentation

import android.os.Bundle
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.navigation.MainScreen
import ru.drrey.babyname.theme.di.ThemeComponent

class MainActivity : BaseActivity() {

    override val featureDependencies = listOf(ThemeComponent::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            router.navigateTo(MainScreen())
        }
    }
}
