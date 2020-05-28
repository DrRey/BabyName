package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_partners_qr_code.*
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.doOnMeasure
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.partners.R
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedFragment


class PartnersQrCodeFragment : ThemedFragment() {

    companion object {
        fun newInstance() = PartnersQrCodeFragment()
    }

    private val viewModel: PartnersViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_partners_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })

        viewModel.loadUserData()
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        themeViewState.accentColorResId?.let { accentColorResId ->
            parentLayout?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    accentColorResId
                )
            )
        }
    }

    private fun renderState(viewState: PartnersViewState) {
        if (viewState.loadUserError != null) {
            Toast.makeText(context, viewState.loadUserError, Toast.LENGTH_SHORT).show()
        } else {
            viewState.userId?.let { userId ->
                showQrCode(userId)
            }
        }
    }

    private fun showQrCode(userId: String) {
        if (qrCodeView.width == 0) {
            qrCodeView.doOnMeasure { showQrCode(userId) }
        } else {
            val qrgEncoder = QRGEncoder(userId, null, QRGContents.Type.TEXT, qrCodeView.width)
            try {
                val bitmap = qrgEncoder.encodeAsBitmap()
                qrCodeView.setImageBitmap(bitmap)
            } catch (e: WriterException) {

            }
        }
    }
}
