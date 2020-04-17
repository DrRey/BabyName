package ru.drrey.babyname.results

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.navigationmediator.ResultsFlowScreenProvider
import ru.drrey.babyname.partners.api.PartnersApi
import ru.drrey.babyname.results.api.ResultsApi
import ru.drrey.babyname.results.di.ResultsDependencies
import ru.drrey.babyname.results.domain.interactor.GetResultsInteractor
import ru.drrey.babyname.results.navigation.ResultsFlowScreenProviderImpl
import ru.drrey.babyname.results.presentation.ResultsViewModel

@ExperimentalCoroutinesApi
object ResultsComponent : FeatureComponent<ResultsDependencies>(), ResultsApi {
    override fun getResultsFlowScreenProvider() = get<ResultsFlowScreenProvider>()

    private val resultsDependenciesModule = module {
        single { (authApi: AuthApi, namesApi: NamesApi, partnersApi: PartnersApi) ->
            ResultsDependencies(authApi, namesApi, partnersApi)
        }
    }

    private val resultsApiModule = module {
        single<ResultsApi> { ResultsComponent }
    }

    private val resultsNavigationModule = module {
        single { ResultsFlowScreenProviderImpl() }
    }

    private val resultsRepositoryModule = module {

    }

    private val resultsInteractorModule = module {
        factory {
            with(get<ResultsDependencies>()) {
                GetResultsInteractor(
                    authApi::getUserId,
                    namesApi::getStars,
                    partnersApi::getPartnerIds,
                    partnersApi::getPartnersStars
                )
            }
        }
    }

    private val resultsViewModelModule = module {
        viewModel { ResultsViewModel(get()) }
    }

    override val modules = listOf(
        resultsDependenciesModule,
        resultsApiModule,
        resultsNavigationModule,
        resultsRepositoryModule,
        resultsInteractorModule,
        resultsViewModelModule
    )
}