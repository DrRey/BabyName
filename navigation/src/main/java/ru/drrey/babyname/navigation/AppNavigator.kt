package ru.drrey.babyname.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.androidx.FragmentScreen

open class AppNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) : com.github.terrakok.cicerone.androidx.AppNavigator(activity, containerId, fragmentManager) {

    override fun commitNewFragmentScreen(screen: FragmentScreen, addToBackStack: Boolean) {
        val currentFragment = fragmentManager.findFragmentById(containerId)
        val fragment = screen.createFragment(fragmentFactory)
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        setupFragmentTransaction(
            screen,
            transaction,
            fragmentManager.findFragmentById(containerId),
            fragment
        )
        if (!shouldAddFragment(currentFragment, fragment)) {
            transaction.replace(containerId, fragment, screen.screenKey)
        } else {
            transaction.add(containerId, fragment, screen.screenKey)
        }
        if (addToBackStack) {
            transaction.addToBackStack(screen.screenKey)
            localStackCopy.add(screen.screenKey)
        }
        transaction.commit()
    }

    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        //not doing animations if first fragment in container
        if (currentFragment == null) {
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


    override fun back() {
        if (localStackCopy.size == 1 && fragmentManager.findFragmentById(containerId)?.parentFragment == null) {
            //not removing last fragment in activity. Exiting activity instead
            localStackCopy.removeLast()
            activityBack()
        } else if (localStackCopy.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy.removeLast()
            onPopBackStack()
        } else {
            activityBack()
        }
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