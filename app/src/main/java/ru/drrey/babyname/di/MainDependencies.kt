package ru.drrey.babyname.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies
import ru.drrey.babyname.names.api.NamesApi
import ru.drrey.babyname.partners.api.PartnersApi

class MainDependencies(val authApi: AuthApi, val partnersApi: PartnersApi, val namesApi: NamesApi) :
    FeatureDependencies