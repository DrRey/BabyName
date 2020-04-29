package ru.drrey.babyname.names.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.data.repository.NamesRepositoryImpl
import ru.drrey.babyname.names.domain.interactor.*
import ru.drrey.babyname.names.domain.repository.NamesRepository
import ru.drrey.babyname.names.navigation.NamesFlowScreenProviderImpl
import ru.drrey.babyname.names.presentation.NamesViewModel
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

object NamesComponent : FeatureComponent<NamesDependencies>(), NamesApi {
    override fun countStarredNamesInteractor(): Interactor<Int, Void?> =
        get<CountStarredNamesInteractor>()

    override fun getStars(userId: String) = get<NamesRepository>().getStars(userId)

    override fun getFlowScreenProvider() = get<NamesFlowScreenProvider>()

    override fun getSexFilterInteractor(): Interactor<Sex?, Void?> = get<GetSexFilterInteractor>()

    override fun setSexFilterInteractor(): Interactor<Nothing, Sex?> = get<SetSexFilterInteractor>()

    private val namesDependenciesModule = module {
        single { (authApi: AuthApi) ->
            NamesDependencies(authApi)
        }
    }

    private val namesApiModule = module {
        single<NamesApi> { NamesComponent }
    }

    private val namesNavigationModule = module {
        single<NamesFlowScreenProvider> { NamesFlowScreenProviderImpl() }
    }

    private val namesRepositoryModule = module {
        single<NamesRepository> { NamesRepositoryImpl(get()) }
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
        factory {
            GetSexFilterInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId
            )
        }
        factory {
            SetSexFilterInteractor(
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