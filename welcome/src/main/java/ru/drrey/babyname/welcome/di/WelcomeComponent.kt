package ru.drrey.babyname.welcome.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.navigationmediator.WelcomeFlowScreenProvider
import ru.drrey.babyname.partners.api.PartnersApi
import ru.drrey.babyname.welcome.api.WelcomeApi
import ru.drrey.babyname.welcome.navigation.WelcomeFlowScreenProviderImpl
import ru.drrey.babyname.welcome.presentation.WelcomeViewModel

object WelcomeComponent : FeatureComponent<WelcomeDependencies>(), WelcomeApi {
    override fun getFlowScreenProvider() = get<WelcomeFlowScreenProvider>()

    private val welcomeDependenciesModule = module {
        single { (authApi: AuthApi, partnersApi: PartnersApi, namesApi: NamesApi) ->
            WelcomeDependencies(authApi, partnersApi, namesApi)
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
        viewModel {
            with(get<WelcomeDependencies>()) {
                WelcomeViewModel(
                    authApi.getUserIdInteractor(),
                    partnersApi.getPartnerIdsListInteractor(),
                    namesApi.getSexFilterInteractor(),
                    namesApi.setSexFilterInteractor()
                )
            }
        }
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