package ru.drrey.babyname.common.presentation.base

import androidx.fragment.app.FragmentActivity
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

abstract class BaseActivity : FragmentActivity() {
    abstract val navigator: Navigator
    private val navigatorHolder: NavigatorHolder by inject()
    protected val router: Router by inject()

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}