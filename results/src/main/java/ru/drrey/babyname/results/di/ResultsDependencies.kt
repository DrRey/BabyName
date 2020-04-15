package ru.drrey.babyname.results.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.partners.api.PartnersApi

class ResultsDependencies(
    val authApi: AuthApi,
    val namesApi: NamesApi,
    val partnersApi: PartnersApi
) : FeatureDependencies