package ru.drrey.babyname.auth.presentation

import ru.drrey.babyname.auth.di.AuthComponent
import ru.drrey.babyname.auth.navigation.AuthFragmentScreen
import ru.drrey.babyname.common.presentation.base.FlowFragment

class AuthFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(AuthComponent::class)

    override fun getFlowScreen() = AuthFragmentScreen()
}