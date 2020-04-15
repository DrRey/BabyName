package ru.drrey.babyname.names.presentation

import ru.drrey.babyname.names.domain.entity.Name

sealed class NamesState

object NamesNotLoaded : NamesState()
object NamesLoading : NamesState()
class NamesLoaded(val names: List<Name>) : NamesState()
class NamesLoadError(val t: Throwable, val message: String?) : NamesState()
class SetStarsError(val name: Name, val position: Int, val t: Throwable, val message: String?) :
    NamesState()

class SetStarsSuccess(val name: Name, val position: Int, val stars: Int) : NamesState()