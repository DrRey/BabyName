package ru.drrey.babyname.common.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.drrey.babyname.common.R
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.navigation.AppNavigator
import kotlin.reflect.KClass


/**
 * Flow fragment
 * Used to scope VMs inside a flow.
 * Launches the provided screen.
 * Doesn't animate first child.
 * Closes if last child fragment exits.
 */
abstract class FlowFragment : RouterFragment() {

    protected open val featureDependencies: List<KClass<*>> = emptyList()

    abstract fun getFlowScreen(): Screen?

    override fun initNavigator() =
        object : AppNavigator(activity!!, childFragmentManager, R.id.fragmentHolder) {
            override fun setupFragmentTransaction(
                screen: FragmentScreen,
                fragmentTransaction: FragmentTransaction,
                currentFragment: Fragment?,
                nextFragment: Fragment
            ) {
                super.setupFragmentTransaction(
                    screen,
                    fragmentTransaction,
                    currentFragment,
                    nextFragment
                )
                if (currentFragment == null) {
                    //removing animation for first fragment. Delay is needed, because child fragments disappear during parent fragment pop animation
                    fragmentTransaction.setCustomAnimations(
                        R.anim.delay_medium,
                        R.anim.delay_medium,
                        R.anim.delay_medium,
                        R.anim.delay_medium
                    )
                }
            }

            override fun onPopBackStack(
                currentFragment: Fragment?,
                nextFragment: Fragment?
            ) {
                if (nextFragment == null) {
                    parentFragment?.router?.exit() ?: activity.router?.exit()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        //preparing dependencies
        featureDependencies.forEach { activity?.router?.prepareFeature(it) }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFlow()
    }

    protected open fun initFlow() {
        if (childFragmentManager.fragments.isEmpty()) {
            getFlowScreen()?.let { router.navigateTo(it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.fragments.forEach {
            it.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }
}