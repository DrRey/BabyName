package ru.drrey.babyname.welcome.di

import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.navigationmediator.WelcomeFlowScreenProvider
import ru.drrey.babyname.welcome.api.WelcomeApi
import ru.drrey.babyname.welcome.navigation.WelcomeFlowScreenProviderImpl

object WelcomeComponent : FeatureComponent<WelcomeDependencies>(), WelcomeApi {
    override fun getFlowScreenProvider() = get<WelcomeFlowScreenProvider>()

    private val welcomeDependenciesModule = module {
        single { (authApi: AuthApi) ->
            WelcomeDependencies(authApi)
        }
    }

    private val welcomeApiModule = module {
        single<WelcomeApi> { WelcomeComponent }
    }

    private val welcomeNavigationModule = module {
        single<WelcomeFlowScreenProvider> { WelcomeFlowScreenProviderImpl() }
    }

    private val welcomeRepositoryModule = module {

    }

    private val welcomeInteractorModule = module {

    }

    private val welcomeViewModelModule = module {

    }

    override val modules = listOf(
        welcomeDependenciesModule,
        welcomeApiModule,
        welcomeNavigationModule,
        welcomeRepositoryModule,
        welcomeInteractorModule,
        welcomeViewModelModule
    )
}