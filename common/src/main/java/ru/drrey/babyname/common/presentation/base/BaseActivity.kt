package ru.drrey.babyname.common.presentation.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.terrakok.cicerone.Cicerone
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.drrey.babyname.common.R
import ru.drrey.babyname.common.presentation.getChildFragmentOrItself
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.navigation.AppNavigator
import ru.drrey.babyname.navigation.Router
import ru.drrey.babyname.navigation.RouterProvider
import kotlin.reflect.KClass

abstract class BaseActivity : FragmentActivity(), RouterProvider {
    override val router: Router by inject()
    val cicerone: Cicerone<Router> by inject { parametersOf("") }
    private val navigator: AppNavigator by lazy { initNavigator() }

    protected open val featureDependencies: List<KClass<*>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        //preparing dependencies
        featureDependencies.forEach { router.prepareFeature(it) }
        super.onCreate(savedInstanceState)
    }

    protected open fun initNavigator(): AppNavigator =
        AppNavigator(this, supportFragmentManager, R.id.fragmentHolder)

    override fun onResumeFragments() {
        super.onResumeFragments()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackButtonPressedProviderFragment) {
            currentFragment.onBackButtonPressed()
        } else {
            currentFragment?.router?.exit() ?: router.exit()
        }
    }

    /**
     * Searches for the currently visible fragment.
     * Returns the current visible fragment or its child.
     */
    private fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.fragmentHolder)?.getChildFragmentOrItself()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
        currentFragment?.onActivityResult(requestCode, resultCode, data)
    }
}