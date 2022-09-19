package ru.drrey.babyname.navigationmediator

import com.github.terrakok.cicerone.Screen
import ru.drrey.babyname.navigation.*
import kotlin.reflect.KClass

class NavigationMediatorImpl(private val featureProvider: FeatureProvider) : NavigationMediator {
    override fun getScreen(flow: Flow): Screen {
        return when (flow) {
            WelcomeFlow -> featureProvider.welcomeFlowScreenProvider.getScreen()
            AuthFlow -> featureProvider.authFlowScreenProvider.getScreen()
            FilterFlow -> featureProvider.filterFlowScreenProvider.getScreen()
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