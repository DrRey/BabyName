package ru.drrey.babyname.names.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.names.data.repository.NamesRepositoryImpl
import ru.drrey.babyname.names.domain.interactor.CountStarredNamesInteractor
import ru.drrey.babyname.names.domain.interactor.GetNamesInteractor
import ru.drrey.babyname.names.domain.interactor.GetNamesWithStarsInteractor
import ru.drrey.babyname.names.domain.interactor.SetStarsInteractor
import ru.drrey.babyname.names.domain.repository.NamesRepository
import ru.drrey.babyname.names.navigation.NamesFlowScreenProviderImpl
import ru.drrey.babyname.names.presentation.NamesViewModel
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

@ExperimentalCoroutinesApi
object NamesComponent : FeatureComponent<NamesDependencies>(), NamesApi {
    override fun countStarredNamesInteractor(): BaseInteractor<Int, Void?> =
        get<CountStarredNamesInteractor>()

    override fun getStars(userId: String) = get<NamesRepository>().getStars(userId)

    override fun getFlowScreenProvider() = get<NamesFlowScreenProvider>()
    private val namesDependenciesModule = module {
        single { (authApi: AuthApi) ->
            NamesDependencies(authApi)
        }
    }

    private val namesApiModule = module {
        single<NamesApi> { NamesComponent }
    }

    private val namesNavigationModule = module {
        single { NamesFlowScreenProviderImpl() }
    }

    private val namesRepositoryModule = module {
        single { NamesRepositoryImpl(get()) }
    }

    private val namesInteractorModule = module {
        factory { GetNamesInteractor(get()) }
        factory {
            GetNamesWithStarsInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId
            )
        }
        factory {
            CountStarredNamesInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId
            )
        }
        factory {
            SetStarsInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId
            )
        }
    }

    private val namesViewModelModule = module {
        viewModel { NamesViewModel(get(), get()) }
    }

    override val modules = listOf(
        namesDependenciesModule,
        namesApiModule,
        namesNavigationModule,
        namesRepositoryModule,
        namesInteractorModule,
        namesViewModelModule
    )
}