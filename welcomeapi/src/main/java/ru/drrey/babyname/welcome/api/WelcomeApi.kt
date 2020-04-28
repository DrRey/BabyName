package ru.drrey.babyname.welcome.api

import ru.drrey.babyname.navigationmediator.WelcomeFlowScreenProvider

interface WelcomeApi {
    fun getFlowScreenProvider(): WelcomeFlowScreenProvider
}