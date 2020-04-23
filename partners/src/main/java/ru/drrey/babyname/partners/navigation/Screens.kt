package ru.drrey.babyname.partners.navigation

import androidx.fragment.app.Fragment
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import ru.drrey.babyname.partners.presentation.AddPartnerFlowFragment
import ru.drrey.babyname.partners.presentation.PartnersQrCodeFlowFragment
import ru.drrey.babyname.partners.presentation.PartnersQrCodeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class PartnersQrCodeScreen : SupportAppScreen() {
    override fun getFragment() = PartnersQrCodeFlowFragment()
}

class PartnersQrCodeFragmentScreen : SupportAppScreen() {
    override fun getFragment() = PartnersQrCodeFragment.newInstance()
}

class AddPartnerScreen : SupportAppScreen() {
    override fun getFragment() = AddPartnerFlowFragment()
}

class AddPartnerFragmentScreen : SupportAppScreen() {
    override fun getFragment(): Fragment =
        BarcodeCapture().shouldAutoFocus(true).setShowDrawRect(false).setShowFlash(false)
            .setSupportMultipleScan(false)
}