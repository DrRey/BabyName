package ru.drrey.babyname.navigationmediator

import kotlin.reflect.KClass

interface FeatureProvider {
    fun prepareFeature(featureApiClass: KClass<*>)
    val welcomeFlowScreenProvider: WelcomeFlowScreenProvider
    val authFlowScreenProvider: AuthFlowScreenProvider
    val namesFlowScreenProvider: NamesFlowScreenProvider
    val partnersQrCodeFlowScreenProvider: PartnersQrCodeFlowScreenProvider
    val addPartnerFlowScreenProvider: AddPartnerFlowScreenProvider
    val resultsFlowScreenProvider: ResultsFlowScreenProvider
}