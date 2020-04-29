package ru.drrey.babyname.welcome.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies
import ru.drrey.babyname.partners.api.PartnersApi

class WelcomeDependencies(val authApi: AuthApi, val partnersApi: PartnersApi) : FeatureDependencies