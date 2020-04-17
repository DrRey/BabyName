package ru.drrey.babyname.partners.navigation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.navigationmediator.PartnersQrCodeFlowScreenProvider
import ru.terrakok.cicerone.Screen

@ExperimentalCoroutinesApi
class PartnersQrCodeFlowScreenProviderImpl : PartnersQrCodeFlowScreenProvider {
    override fun getScreen(): Screen {
        return PartnersQrCodeScreen()
    }
}