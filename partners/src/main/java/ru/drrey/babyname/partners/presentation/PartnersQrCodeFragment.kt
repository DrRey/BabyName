package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.partners_qr_code_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.partners.R


class PartnersQrCodeFragment : Fragment() {

    companion object {
        fun newInstance() = PartnersQrCodeFragment()
    }

    private val viewModel: PartnersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.partners_qr_code_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                Initial -> {

                }
                is GetUserError -> {

                }
                is GetUserSuccess -> {
                    showQrCode(it.userId)
                }
                is PartnerAddError -> {

                }
                PartnerAddSuccess -> {

                }
            }
        })

        viewModel.loadUserData()
    }

    private fun showQrCode(userId: String) {
        val qrgEncoder = QRGEncoder(userId, null, QRGContents.Type.TEXT, qrCodeView.width)
        try {
            val bitmap = qrgEncoder.encodeAsBitmap()
            qrCodeView.setImageBitmap(bitmap)
        } catch (e: WriterException) {

        }
    }
}
