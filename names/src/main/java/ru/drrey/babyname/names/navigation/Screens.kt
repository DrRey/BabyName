package ru.drrey.babyname.names.navigation

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.navigation.AppScreen
import ru.drrey.babyname.names.presentation.NamesActivity

@ExperimentalCoroutinesApi
class NamesScreen : AppScreen() {
    override fun getActivityIntent(context: Context): Intent {
        return Intent(context, NamesActivity::class.java)
    }
}