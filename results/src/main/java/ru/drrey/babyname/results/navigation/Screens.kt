package ru.drrey.babyname.results.navigation

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.navigation.AppScreen
import ru.drrey.babyname.results.presentation.ResultsActivity

@ExperimentalCoroutinesApi
class ResultsScreen : AppScreen() {
    override fun getActivityIntent(context: Context): Intent? {
        return Intent(context, ResultsActivity::class.java)
    }
}