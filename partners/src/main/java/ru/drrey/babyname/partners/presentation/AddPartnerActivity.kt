package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.util.SparseArray
import androidx.lifecycle.Observer
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.navigation.AppNavigator
import ru.drrey.babyname.partners.R
import ru.terrakok.cicerone.Navigator
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever


@ExperimentalCoroutinesApi
class AddPartnerActivity : BaseActivity() {
    override val navigator: Navigator
        get() = AppNavigator(this, R.id.parentLayout)

    private val viewModel: PartnersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_partner_activity)
        viewModel.getState().observe(this, Observer {
            when (it) {
                Initial -> {

                }
                is GetUserError -> {

                }
                is GetUserSuccess -> {

                }
                is PartnerAddError -> {

                }
                PartnerAddSuccess -> {
                    finish()
                }
            }
        })

        val barcodeCapture =
            supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture?
        barcodeCapture?.setRetrieval(object : BarcodeRetriever {
            override fun onRetrieved(barcode: Barcode?) {
                barcode?.let {
                    viewModel.onAddPartner(it.displayValue)
                }
            }

            override fun onRetrievedMultiple(
                closetToClick: Barcode?,
                barcode: MutableList<BarcodeGraphic>?
            ) {

            }

            override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {

            }

            override fun onRetrievedFailed(reason: String?) {

            }

            override fun onPermissionRequestDenied() {

            }
        })
    }
}
