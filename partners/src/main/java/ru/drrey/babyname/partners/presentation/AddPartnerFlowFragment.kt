package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
import com.google.android.gms.vision.barcode.Barcode
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.partners.R
import ru.drrey.babyname.partners.di.PartnersComponent
import ru.drrey.babyname.partners.navigation.AddPartnerFragmentScreen
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever

class AddPartnerFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(PartnersComponent::class)

    private val viewModel: PartnersViewModel by viewModel()

    override fun getFlowScreen() = AddPartnerFragmentScreen()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            when (it) {
                is PartnersViewEvent.PartnerAddError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                PartnersViewEvent.PartnerAdded -> {
                    router.exit()
                }
            }
        })

        val barcodeCapture =
            childFragmentManager.findFragmentById(R.id.fragmentHolder) as BarcodeCapture?
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