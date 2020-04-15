package ru.drrey.babyname.auth.presentation

sealed class AuthState

object AuthUndefined : AuthState()
object AuthLoading : AuthState()
object AuthNone : AuthState()
class AuthError(val t: Throwable?, val message: String?) : AuthState()
class AuthComplete(val userId: String) : AuthState()