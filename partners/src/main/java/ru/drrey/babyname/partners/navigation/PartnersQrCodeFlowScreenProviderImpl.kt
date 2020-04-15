package ru.drrey.babyname.partners.navigation

import ru.drrey.babyname.navigationmediator.PartnersQrCodeFlowScreenProvider
import ru.terrakok.cicerone.Screen

class PartnersQrCodeFlowScreenProviderImpl : PartnersQrCodeFlowScreenProvider {
    override fun getScreen(): Screen {
        return PartnersQrCodeScreen()
    }
}