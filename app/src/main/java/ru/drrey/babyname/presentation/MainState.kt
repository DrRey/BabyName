package ru.drrey.babyname.presentation

sealed class MainState

object NotLoaded : MainState()
object Loading : MainState()
class Loaded(val userId: String, val partnerIds: List<String>, val starredNames: Int) : MainState()
class LoadError(
    val userId: String?,
    val partnerIds: List<String>?,
    val starredNames: Int?,
    val t: Throwable,
    val message: String?
) : MainState()