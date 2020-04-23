package ru.drrey.babyname.navigationmediator

import ru.drrey.babyname.navigation.*
import ru.terrakok.cicerone.Screen
import kotlin.reflect.KClass

class NavigationMediatorImpl(private val featureProvider: FeatureProvider) : NavigationMediator {
    override fun getScreen(flow: Flow): Screen {
        return when (flow) {
            AuthFlow -> featureProvider.authFlowScreenProvider.getScreen()
            NamesFlow -> featureProvider.namesFlowScreenProvider.getScreen()
            PartnersQrCodeFlow -> featureProvider.partnersQrCodeFlowScreenProvider.getScreen()
            AddPartnerFlow -> featureProvider.addPartnerFlowScreenProvider.getScreen()
            ResultsFlow -> featureProvider.resultsFlowScreenProvider.getScreen()
        }
    }

    override fun prepareFeature(featureApiClass: KClass<*>) {
        featureProvider.prepareFeature(featureApiClass)
    }
}