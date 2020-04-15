package ru.drrey.babyname.partners.navigation

import ru.drrey.babyname.navigationmediator.AddPartnerFlowScreenProvider
import ru.terrakok.cicerone.Screen

class AddPartnerFlowScreenProviderImpl : AddPartnerFlowScreenProvider {
    override fun getScreen(): Screen {
        return AddPartnerScreen()
    }
}