package ru.drrey.babyname.navigationmediator

import ru.drrey.babyname.navigation.*
import ru.terrakok.cicerone.Screen

class NavigationMediatorImpl(private val featureProvider: FeatureProvider) : NavigationMediator {
    override fun getScreen(flow: Flow): Screen? {
        return if (flow is AppFlow) {
            when (flow) {
                AuthFlow -> featureProvider.authFlowScreenProvider.getScreen()
                NamesFlow -> featureProvider.namesFlowScreenProvider.getScreen()
                PartnersQrCodeFlow -> featureProvider.partnersQrCodeFlowScreenProvider.getScreen()
                AddPartnerFlow -> featureProvider.addPartnerFlowScreenProvider.getScreen()
                ResultsFlow -> featureProvider.resultsFlowScreenProvider.getScreen()
            }
        } else {
            null
        }
    }
}