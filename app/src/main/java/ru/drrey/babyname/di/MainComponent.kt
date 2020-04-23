package ru.drrey.babyname.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.partners.api.PartnersApi
import ru.drrey.babyname.presentation.MainViewModel

object MainComponent : FeatureComponent<MainDependencies>() {

    private val mainDependenciesModule = module {
        single { (authApi: AuthApi, partnersApi: PartnersApi, namesApi: NamesApi) ->
            MainDependencies(authApi, partnersApi, namesApi)
        }
    }

    private val mainViewModelModule = module {
        viewModel {
            with(get<MainDependencies>()) {
                MainViewModel(
                    authApi.getUserIdInteractor(),
                    partnersApi.getPartnerIdsListInteractor(),
                    partnersApi.clearPartnersInteractor(),
                    namesApi.countStarredNamesInteractor()
                )
            }
        }
    }

    override val modules = listOf(
        mainDependenciesModule,
        mainViewModelModule
    )
}