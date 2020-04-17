package ru.drrey.babyname.auth.presentation

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.auth.R
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.navigation.AppNavigator
import ru.terrakok.cicerone.Navigator

class AuthActivity : BaseActivity() {
    override val navigator: Navigator
        get() = AppNavigator(this, R.id.parentLayout)

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.parentLayout, AuthFragment.newInstance())
                .commitNow()
        }

        authViewModel.getViewState().observe(this, NonNullObserver {
            if (!it.isLoading && it.isLoaded && it.userId != null) {
                finish()
            }
        })
    }
}
