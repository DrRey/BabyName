package ru.drrey.babyname.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward

open class AppNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) :
    SupportAppNavigator(activity, fragmentManager, containerId) {
    override fun fragmentForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val currentFragment = fragmentManager.findFragmentById(containerId)

        val fragmentParams = screen.fragmentParams
        val fragment = if (fragmentParams == null) createFragment(screen) else null

        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()

        setupFragmentTransaction(
            command,
            currentFragment,
            fragment,
            fragmentTransaction
        )

        if (shouldAddFragment(currentFragment, fragment)) {
            if (fragmentParams != null) {
                fragmentTransaction.add(
                    containerId,
                    fragmentParams.fragmentClass,
                    fragmentParams.arguments
                )
            } else {
                fragmentTransaction.add(containerId, fragment!!)
            }
        } else {
            if (fragmentParams != null) {
                fragmentTransaction.replace(
                    containerId,
                    fragmentParams.fragmentClass,
                    fragmentParams.arguments
                )
            } else {
                fragmentTransaction.replace(containerId, fragment!!)
            }
        }

        fragmentTransaction
            .addToBackStack(screen.screenKey)
            .commit()
        localStackCopy.add(screen.screenKey)
    }

    override fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) {
        //not doing animations if first fragment in container and not SecondaryNavigationFragment
        //or TertiaryNavigationFragment or QuaternaryNavigationFragment
        if (currentFragment == null && nextFragment !is SecondaryNavigationFragment &&
            nextFragment !is TertiaryNavigationFragment && nextFragment !is QuaternaryNavigationFragment
        ) {
            return
        }

        when (nextFragment) {
            is AlphaNavigationFragment -> fragmentTransaction.setCustomAnimations(
                R.anim.alpha_in,
                R.anim.alpha_out,
                R.anim.alpha_in,
                R.anim.alpha_out
            )
            else -> fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }

    private fun shouldAddFragment(currentFragment: Fragment?, fragment: Fragment?) =
        (fragment is SecondaryNavigationFragment && currentFragment !is SecondaryNavigationFragment && currentFragment !is TertiaryNavigationFragment && currentFragment !is QuaternaryNavigationFragment) ||
                (fragment is TertiaryNavigationFragment && currentFragment !is TertiaryNavigationFragment && currentFragment !is QuaternaryNavigationFragment) ||
                (fragment is QuaternaryNavigationFragment && currentFragment !is QuaternaryNavigationFragment)

    override fun fragmentBack() {
        super.fragmentBack()
        onPopBackStack()
    }

    override fun backTo(command: BackTo) {
        super.backTo(command)
        onPopBackStack(command.screen == null, command.screen?.screenKey)
    }

    private fun onPopBackStack(toRoot: Boolean = false, nextFragmentKey: String? = null) {
        val currentFragment = fragmentManager.findFragmentById(containerId)
        val nextFragment = if (toRoot) null else fragmentManager.findFragmentByTag(
            nextFragmentKey ?: localStackCopy.elementAtOrNull(localStackCopy.size - 2)
        )
        onPopBackStack(currentFragment, nextFragment)
    }

    /**
     * Fired on back navigation after fragmentManager.popBackStack is called.
     */
    protected open fun onPopBackStack(currentFragment: Fragment?, nextFragment: Fragment?) {

    }
}