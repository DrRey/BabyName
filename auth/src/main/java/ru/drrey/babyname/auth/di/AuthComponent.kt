package ru.drrey.babyname.auth.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.auth.data.AuthRepositoryImpl
import ru.drrey.babyname.auth.domain.interactor.GetUserIdInteractor
import ru.drrey.babyname.auth.domain.interactor.SetUserIdInteractor
import ru.drrey.babyname.auth.domain.repository.AuthRepository
import ru.drrey.babyname.auth.navigation.AuthFlowScreenProviderImpl
import ru.drrey.babyname.auth.presentation.AuthViewModel
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.common.domain.interactor.base.BaseInteractor
import ru.drrey.babyname.navigationmediator.AuthFlowScreenProvider

object AuthComponent : FeatureComponent<AuthDependencies>(), AuthApi {
    override fun getFlowScreenProvider() = get<AuthFlowScreenProvider>()
    override fun getUserIdInteractor(): BaseInteractor<String, Void?> = get<GetUserIdInteractor>()
    override fun getUserId() = get<AuthRepository>().getUserId()

    private val authDependenciesModule = module {
        single { AuthDependencies() }
    }

    private val authApiModule = module {
        single<AuthApi> { AuthComponent }
    }

    private val authNavigationModule = module {
        single { AuthFlowScreenProviderImpl() }
    }

    private val authRepositoryModule = module {
        single { AuthRepositoryImpl(get()) }
    }

    private val authInteractorModule = module {
        factory { GetUserIdInteractor(get(), get(), get()) }
        factory { SetUserIdInteractor(get(), get(), get()) }
    }

    private val authViewModelModule = module {
        viewModel { AuthViewModel(get(), get()) }
    }

    override val modules = listOf(
        authDependenciesModule,
        authApiModule,
        authNavigationModule,
        authRepositoryModule,
        authInteractorModule,
        authViewModelModule
    )
}