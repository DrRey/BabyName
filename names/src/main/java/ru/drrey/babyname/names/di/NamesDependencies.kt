package ru.drrey.babyname.names.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies

class NamesDependencies(val authApi: AuthApi) : FeatureDependencies