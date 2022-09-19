package ru.drrey.babyname.navigation

import com.github.terrakok.cicerone.Screen

interface FlowScreenProvider

interface SimpleFlowScreenProvider : FlowScreenProvider {
    fun getScreen(): Screen
}

interface DataFlowScreenProvider<T> : FlowScreenProvider {
    fun getScreen(data: T): Screen
}