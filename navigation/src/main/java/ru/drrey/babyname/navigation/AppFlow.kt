package ru.drrey.babyname.navigation

sealed class AppFlow : Flow

object AuthFlow : AppFlow()

object NamesFlow : AppFlow()

object PartnersQrCodeFlow : AppFlow()
object AddPartnerFlow : AppFlow()

object ResultsFlow : AppFlow()