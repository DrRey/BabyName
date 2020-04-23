package ru.drrey.babyname.partners.presentation

import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.partners.di.PartnersComponent
import ru.drrey.babyname.partners.navigation.PartnersQrCodeFragmentScreen

class PartnersQrCodeFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(PartnersComponent::class)

    override fun getFlowScreen() = PartnersQrCodeFragmentScreen()
}