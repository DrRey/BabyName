package ru.drrey.babyname.welcome.di

import ru.drrey.babyname.auth.api.AuthApi
import ru.drrey.babyname.common.di.FeatureDependencies

class WelcomeDependencies(val authApi: AuthApi) : FeatureDependencies