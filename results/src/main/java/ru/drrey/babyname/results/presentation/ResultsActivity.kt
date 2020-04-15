package ru.drrey.babyname.results.presentation

import android.os.Bundle
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.navigation.AppNavigator
import ru.drrey.babyname.results.R
import ru.terrakok.cicerone.Navigator

class ResultsActivity : BaseActivity() {
    override val navigator: Navigator
        get() = AppNavigator(this, R.id.parentLayout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.parentLayout,
                    ResultsFragment.newInstance()
                )
                .commitNow()
        }
    }

}
