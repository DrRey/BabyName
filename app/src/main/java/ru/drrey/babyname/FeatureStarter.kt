package ru.drrey.babyname

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.auth.di.AuthComponent
import ru.drrey.babyname.di.MainComponent
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.names.di.NamesComponent
import ru.drrey.babyname.navigationmediator.*
import ru.drrey.babyname.partners.api.PartnersApi
import ru.drrey.babyname.partners.di.PartnersComponent
import ru.drrey.babyname.results.ResultsComponent
import ru.drrey.babyname.results.api.ResultsApi
import ru.drrey.babyname.theme.di.ThemeComponent
import ru.drrey.babyname.welcome.api.WelcomeApi
import ru.drrey.babyname.welcome.di.WelcomeComponent
import kotlin.reflect.KClass

object FeatureStarter : FeatureProvider, KoinComponent {
    override val welcomeFlowScreenProvider: WelcomeFlowScreenProvider
        get() = getWelcomeFeature().getFlowScreenProvider()
    override val authFlowScreenProvider: AuthFlowScreenProvider
        get() = getAuthFeature().getFlowScreenProvider()
    override val filterFlowScreenProvider: FilterFlowScreenProvider
        get() = getNamesFeature().getFilterFlowScreenProvider()
    override val namesFlowScreenProvider: NamesFlowScreenProvider
        get() = getNamesFeature().getNamesFlowScreenProvider()
    override val partnersQrCodeFlowScreenProvider: PartnersQrCodeFlowScreenProvider
        get() = getPartnersFeature().getPartnersQrCodeFlowScreenProvider()
    override val addPartnerFlowScreenProvider: AddPartnerFlowScreenProvider
        get() = getPartnersFeature().getAddPartnersFlowScreenProvider()
    override val resultsFlowScreenProvider: ResultsFlowScreenProvider
        get() = getResultsFeature().getResultsFlowScreenProvider()

    override fun prepareFeature(featureApiClass: KClass<*>) {
        when (featureApiClass.qualifiedName) {
            ThemeComponent::class.qualifiedName -> initThemeFeature()
            MainComponent::class.qualifiedName -> initMainFeature()
            AuthApi::class.qualifiedName -> getAuthFeature()
            NamesApi::class.qualifiedName -> getNamesFeature()
            PartnersApi::class.qualifiedName -> getPartnersFeature()
            ResultsApi::class.qualifiedName -> getResultsFeature()
        }
    }

    fun initThemeFeature() {
        ThemeComponent.load()?.init(get())
    }

    fun initMainFeature() {
        MainComponent.load()?.init(get {
            parametersOf(
                getAuthFeature(),
                getPartnersFeature(),
                getNamesFeature()
            )
        })
    }

    fun getWelcomeFeature(): WelcomeApi {
        WelcomeComponent.load()?.init(get {
            parametersOf(
                getAuthFeature(),
                getPartnersFeature(),
                getNamesFeature()
            )
        })
        return get()
    }

    fun getAuthFeature(): AuthApi {
        AuthComponent.load()?.init(get())
        return get()
    }

    fun getNamesFeature(): NamesApi {
        NamesComponent.load()?.init(get {
            parametersOf(
                getAuthFeature()
            )
        })
        return get()
    }

    fun getPartnersFeature(): PartnersApi {
        PartnersComponent.load()?.init(get {
            parametersOf(
                getAuthFeature()
            )
        })
        return get()
    }

    fun getResultsFeature(): ResultsApi {
        ResultsComponent.load()?.init(get {
            parametersOf(
                getAuthFeature(),
                getNamesFeature(),
                getPartnersFeature()
            )
        })
        return get()
    }
}