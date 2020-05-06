package ru.drrey.babyname.presentation

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.navigation.MainScreen
import ru.drrey.babyname.theme.api.ThemeViewModelApi
import ru.drrey.babyname.theme.di.ThemeComponent

class MainActivity : BaseActivity() {

    override val featureDependencies = listOf(ThemeComponent::class)

    private val themeViewModel: ThemeViewModelApi by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeViewModel.getViewState().observe(this, NonNullObserver {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor =
                    ContextCompat.getColor(this, it.accentColorResId)
            }
        })

        if (savedInstanceState == null) {
            router.navigateTo(MainScreen())
        }
    }
}
