package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import ru.drrey.babyname.common.presentation.base.ViewBindingFragment
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.partners.databinding.FragmentAddPartnerBinding

class AddPartnerFragment : ViewBindingFragment<FragmentAddPartnerBinding>() {

    override val viewBinder: (LayoutInflater) -> FragmentAddPartnerBinding =
        { FragmentAddPartnerBinding.inflate(it) }

    private val viewModel: PartnersViewModel by sharedParentViewModel()

    private var codeScanner: CodeScanner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeScanner = CodeScanner(requireContext(), viewBinding!!.codeScannerView)
        codeScanner?.decodeCallback = DecodeCallback {
            viewModel.onAddPartner(it.text)
        }
        codeScanner?.errorCallback = ErrorCallback.SUPPRESS
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}