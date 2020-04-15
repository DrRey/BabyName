package ru.drrey.babyname.partners.presentation

sealed class PartnersState

object Initial : PartnersState()
class GetUserError(val t: Throwable, val message: String?) : PartnersState()
class GetUserSuccess(val userId: String) : PartnersState()
class PartnerAddError(val t: Throwable, val message: String?) : PartnersState()
object PartnerAddSuccess : PartnersState()