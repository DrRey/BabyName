package ru.drrey.babyname.navigation

import ru.terrakok.cicerone.Screen

interface NavigationMediator {
    fun getScreen(flow: Flow): Screen?
}