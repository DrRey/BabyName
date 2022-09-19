package ru.drrey.babyname.common.presentation.base

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.drrey.babyname.navigation.AppNavigator
import ru.drrey.babyname.navigation.Router
import ru.drrey.babyname.navigation.RouterProvider

abstract class RouterFragment : Fragment(), RouterProvider {
    override val router: Router by lazy { cicerone.router }

    private val cicerone: Cicerone<Router> by inject { parametersOf(this::class.simpleName + this.hashCode()) }
    private val navigator: AppNavigator by lazy { initNavigator() }

    protected abstract fun initNavigator(): AppNavigator

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }
}