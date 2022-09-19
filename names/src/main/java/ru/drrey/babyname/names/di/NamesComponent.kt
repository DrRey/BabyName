package ru.drrey.babyname.names.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.common.domain.interactor.base.Interactor
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.names.data.repository.NamesRepositoryImpl
import ru.drrey.babyname.names.domain.interactor.*
import ru.drrey.babyname.names.domain.repository.NamesRepository
import ru.drrey.babyname.names.navigation.FilterFlowScreenProviderImpl
import ru.drrey.babyname.names.navigation.NamesFlowScreenProviderImpl
import ru.drrey.babyname.names.presentation.FilterViewModel
import ru.drrey.babyname.names.presentation.NamesViewModel
import ru.drrey.babyname.navigationmediator.FilterFlowScreenProvider
import ru.drrey.babyname.navigationmediator.NamesFlowScreenProvider

object NamesComponent : FeatureComponent<NamesDependencies>(), NamesApi {
    override fun countStarredNamesInteractor(): Interactor<Int, Nothing?> =
        get<CountStarredNamesInteractor>()

    override fun getStars(userId: String) = get<NamesRepository>().getStars(userId)

    override fun getNamesFlowScreenProvider() = get<NamesFlowScreenProvider>()

    override fun getFilterFlowScreenProvider() = get<FilterFlowScreenProvider>()

    override fun getSexFilterInteractor(): Interactor<Sex?, Nothing?> =
        get<GetSexFilterInteractor>()

    override fun setSexFilterInteractor(): Interactor<Unit, Sex?> = get<SetSexFilterInteractor>()

    override fun countUnfilteredNamesInteractor(): Interactor<Int, Nothing?> =
        get<CountUnfilteredNamesInteractor>()

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
        single<FilterFlowScreenProvider> { FilterFlowScreenProviderImpl() }
    }

    private val namesRepositoryModule = module {
        single<NamesRepository> { NamesRepositoryImpl(get()) }
    }

    private val namesInteractorModule = module {
        factory { GetNamesInteractor(get(), get()) }
        factory {
            GetNamesWithStarsInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId,
                get()
            )
        }
        factory {
            CountStarredNamesInteractor(
                get()
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
        factory {
            GetUnfilteredNamesInteractor(get())
        }
        factory {
            CountUnfilteredNamesInteractor(get())
        }
        factory {
            GetFilteredNamesInteractor(get())
        }
        factory {
            SetNameFilterInteractor(
                get(),
                get<NamesDependencies>().authApi::getUserId
            )
        }
    }

    private val namesViewModelModule = module {
        viewModel { NamesViewModel(get(), get()) }
        viewModel { FilterViewModel(get(), get()) }
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