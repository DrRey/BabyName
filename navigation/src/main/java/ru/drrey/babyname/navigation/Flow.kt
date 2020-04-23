package ru.drrey.babyname.navigation

sealed class Flow
object AuthFlow : Flow()
object NamesFlow : Flow()
object PartnersQrCodeFlow : Flow()
object AddPartnerFlow : Flow()
object ResultsFlow : Flow()