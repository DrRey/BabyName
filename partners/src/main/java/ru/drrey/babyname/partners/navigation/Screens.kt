package ru.drrey.babyname.partners.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.drrey.babyname.common.navigation.AppScreen
import ru.drrey.babyname.partners.presentation.AddPartnerActivity
import ru.drrey.babyname.partners.presentation.PartnersQrCodeFragment

@ExperimentalCoroutinesApi
class PartnersQrCodeScreen : AppScreen() {
    override fun getFragment(): Fragment {
        return PartnersQrCodeFragment.newInstance()
    }
}

class AddPartnerScreen : AppScreen() {
    override fun getActivityIntent(context: Context): Intent? {
        return Intent(context, AddPartnerActivity::class.java)
    }
}