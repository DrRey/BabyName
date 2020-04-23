package ru.drrey.babyname.partners.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.dsl.module
import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.navigationmediator.AddPartnerFlowScreenProvider
import ru.drrey.babyname.navigationmediator.PartnersQrCodeFlowScreenProvider
import ru.drrey.babyname.partners.api.PartnersApi
import ru.drrey.babyname.partners.data.repository.PartnersRepositoryImpl
import ru.drrey.babyname.partners.domain.interactor.AddPartnerInteractor
import ru.drrey.babyname.partners.domain.interactor.ClearPartnersInteractor
import ru.drrey.babyname.partners.domain.interactor.GetPartnerIdsListInteractor
import ru.drrey.babyname.partners.domain.interactor.GetPartnersListInteractor
import ru.drrey.babyname.partners.domain.repository.PartnersRepository
import ru.drrey.babyname.partners.navigation.AddPartnerFlowScreenProviderImpl
import ru.drrey.babyname.partners.navigation.PartnersQrCodeFlowScreenProviderImpl
import ru.drrey.babyname.partners.presentation.PartnersViewModel

object PartnersComponent : FeatureComponent<PartnersDependencies>(), PartnersApi {
    override fun getPartnersQrCodeFlowScreenProvider() = get<PartnersQrCodeFlowScreenProvider>()
    override fun getAddPartnersFlowScreenProvider() = get<AddPartnerFlowScreenProvider>()

    override fun clearPartnersInteractor() = get<ClearPartnersInteractor>()
    override fun getPartnerIdsListInteractor() = get<GetPartnerIdsListInteractor>()

    override fun getPartnerIds(userId: String): Flow<List<String>> =
        getKoin().get<PartnersRepository>().getPartnersList(userId)
            .map { it.map { partner -> partner.id } }

    override fun getPartnersStars(partnerIds: List<String>) =
        get<PartnersRepository>().getPartnersStars(partnerIds)

    private val partnersDependenciesModule = module {
        single { (authApi: AuthApi) ->
            PartnersDependencies(authApi)
        }
    }

    private val partnersApiModule = module {
        single<PartnersApi> { PartnersComponent }
    }

    private val partnersNavigationModule = module {
        single<PartnersQrCodeFlowScreenProvider> { PartnersQrCodeFlowScreenProviderImpl() }
        single<AddPartnerFlowScreenProvider> { AddPartnerFlowScreenProviderImpl() }
    }

    private val partnersRepositoryModule = module {
        single<PartnersRepository> { PartnersRepositoryImpl(get()) }
    }

    private val partnersInteractorModule = module {
        factory {
            ClearPartnersInteractor(
                get(),
                get<PartnersDependencies>().authApi::getUserId
            )
        }
        factory {
            AddPartnerInteractor(
                get(),
                get<PartnersDependencies>().authApi::getUserId
            )
        }
        factory {
            GetPartnersListInteractor(
                get(),
                get<PartnersDependencies>().authApi::getUserId
            )
        }
        factory {
            GetPartnerIdsListInteractor(
                get(),
                get<PartnersDependencies>().authApi::getUserId
            )
        }
    }

    private val partnersViewModelModule = module {
        viewModel {
            PartnersViewModel(
                get<PartnersDependencies>().authApi.getUserIdInteractor(),
                get()
            )
        }
    }

    override val modules = listOf(
        partnersDependenciesModule,
        partnersApiModule,
        partnersNavigationModule,
        partnersRepositoryModule,
        partnersInteractorModule,
        partnersViewModelModule
    )
}