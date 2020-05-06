package ru.drrey.babyname.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.navigation.AddPartnerFlow
import ru.drrey.babyname.navigation.AuthFlow
import ru.drrey.babyname.navigation.PartnersQrCodeFlow
import ru.drrey.babyname.theme.api.ThemeViewModelApi
import ru.drrey.babyname.welcome.R

class WelcomeFragment : Fragment() {

    private val themeViewModel: ThemeViewModelApi by sharedViewModel()
    private val viewModel: WelcomeViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPartnerView?.apply {
            text = getString(R.string.add_partner)
            setOnClickListener { activity?.router?.startFlow(AddPartnerFlow) }
        }
        partnerQrCodeView?.apply {
            text = getString(R.string.partner_qr_code)
            setOnClickListener { activity?.router?.startFlow(PartnersQrCodeFlow) }
        }
        partnerLaterView?.apply {
            text = getString(R.string.maybe_later)
            setOnClickListener { viewModel.onPartnersFinished() }
        }

        girlSexView?.apply {
            text = getString(R.string.girl)
            setOnClickListener {
                viewModel.onSexSet(Sex.GIRL)
                themeViewModel.onAccentColorChange(R.color.pink)
            }
        }
        boySexView?.apply {
            text = getString(R.string.boy)
            setOnClickListener {
                viewModel.onSexSet(Sex.BOY)
                themeViewModel.onAccentColorChange(R.color.blue)
            }
        }
        noSexView?.apply {
            text = getString(R.string.dont_know_yet)
            setOnClickListener {
                viewModel.onSexSet(null)
                themeViewModel.onAccentColorChange(R.color.green)
            }
        }

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.startWelcome()
    }

    private fun renderState(viewState: WelcomeViewState) {
        if (viewState.textShown) {
            textView?.apply {
                viewState.textResId?.let { text = getString(it) }
                visibility = View.VISIBLE
            }
        } else {
            textView?.visibility = View.GONE
        }
        partnerButtonsGroup?.visibility =
            if (viewState.partnerButtonsShown) View.VISIBLE else View.GONE
        sexButtonsGroup?.visibility = if (viewState.sexButtonsShown) View.VISIBLE else View.GONE
    }

    private fun actOnEvent(viewEvent: WelcomeViewEvent) {
        when (viewEvent) {
            WelcomeViewEvent.StartAuth -> {
                activity?.router?.startFlow(AuthFlow)
            }
            WelcomeViewEvent.WelcomeFinished -> {
                router?.exit()
            }
        }
    }
}
