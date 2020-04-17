package ru.drrey.babyname.names.presentation

import android.os.Bundle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.names.R
import ru.drrey.babyname.navigation.AppNavigator
import ru.terrakok.cicerone.Navigator

@ExperimentalCoroutinesApi
class NamesActivity : BaseActivity() {
    override val navigator: Navigator
        get() = AppNavigator(this, R.id.parentLayout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.names_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.parentLayout,
                    NamesFragment.newInstance()
                )
                .commitNow()
        }
    }

}
