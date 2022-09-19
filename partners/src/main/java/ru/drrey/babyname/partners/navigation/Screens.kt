package ru.drrey.babyname.partners.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import ru.drrey.babyname.partners.presentation.AddPartnerFlowFragment
import ru.drrey.babyname.partners.presentation.PartnersQrCodeFlowFragment
import ru.drrey.babyname.partners.presentation.PartnersQrCodeFragment

class PartnersQrCodeScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = PartnersQrCodeFlowFragment()
}

class PartnersQrCodeFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = PartnersQrCodeFragment.newInstance()
}

class AddPartnerScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory) = AddPartnerFlowFragment()
}

class AddPartnerFragmentScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        BarcodeCapture().shouldAutoFocus(true).setShowDrawRect(false).setShowFlash(false)
            .setSupportMultipleScan(false)
}