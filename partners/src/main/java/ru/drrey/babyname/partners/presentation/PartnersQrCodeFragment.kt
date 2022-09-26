package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import com.google.zxing.WriterException
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.doOnMeasure
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.partners.databinding.FragmentPartnersQrCodeBinding
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedBindingFragment


class PartnersQrCodeFragment : ThemedBindingFragment<FragmentPartnersQrCodeBinding>() {

    companion object {
        fun newInstance() = PartnersQrCodeFragment()
    }

    override val viewBinder: (LayoutInflater) -> FragmentPartnersQrCodeBinding =
        { FragmentPartnersQrCodeBinding.inflate(it) }

    private val viewModel: PartnersViewModel by sharedParentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })

        viewModel.loadUserData()
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        themeViewState.accentColorResId?.let { accentColorResId ->
            viewBinding?.parentLayout?.setBackgroundColor(
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
        if (viewBinding?.qrCodeView?.width == 0) {
            viewBinding?.qrCodeView?.doOnMeasure { showQrCode(userId) }
        } else {
            val qrgEncoder =
                QRGEncoder(userId, null, QRGContents.Type.TEXT, viewBinding?.qrCodeView?.width ?: 0)
            try {
                val bitmap = qrgEncoder.bitmap
                viewBinding?.qrCodeView?.setImageBitmap(bitmap)
            } catch (_: WriterException) {

            }
        }
    }
}
