package ru.drrey.babyname.partners.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies

class PartnersDependencies(val authApi: AuthApi) : FeatureDependencies