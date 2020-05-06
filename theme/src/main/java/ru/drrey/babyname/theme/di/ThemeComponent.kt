package ru.drrey.babyname.theme.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.drrey.babyname.common.di.FeatureComponent
import ru.drrey.babyname.theme.api.ThemeApi
import ru.drrey.babyname.theme.api.ThemeViewModelApi
import ru.drrey.babyname.theme.data.repository.ThemeRepositoryImpl
import ru.drrey.babyname.theme.domain.interactor.GetAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.GetPrimaryColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SaveAccentColorInteractor
import ru.drrey.babyname.theme.domain.interactor.SavePrimaryColorInteractor
import ru.drrey.babyname.theme.domain.repository.ThemeRepository
import ru.drrey.babyname.theme.presentation.ThemeViewModel

object ThemeComponent : FeatureComponent<ThemeDependencies>(), ThemeApi {

    private val themeDependenciesModule = module {
        single { ThemeDependencies() }
    }

    private val themeApiModule = module {
        single<ThemeApi> { ThemeComponent }
    }

    private val themeRepositoryModule = module {
        single<ThemeRepository> { ThemeRepositoryImpl(get()) }
    }

    private val themeInteractorModule = module {
        factory { GetPrimaryColorInteractor(get()) }
        factory { GetAccentColorInteractor(get()) }
        factory { SavePrimaryColorInteractor(get()) }
        factory { SaveAccentColorInteractor(get()) }
    }

    private val themeViewModelModule = module {
        viewModel<ThemeViewModelApi> {
            ThemeViewModel(get(), get(), get(), get())
        }
    }

    override val modules = listOf(
        themeDependenciesModule,
        themeApiModule,
        themeRepositoryModule,
        themeInteractorModule,
        themeViewModelModule
    )
}