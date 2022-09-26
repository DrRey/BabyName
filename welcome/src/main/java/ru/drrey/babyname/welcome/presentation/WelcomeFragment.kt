package ru.drrey.babyname.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.navigation.AddPartnerFlow
import ru.drrey.babyname.navigation.AuthFlow
import ru.drrey.babyname.navigation.PartnersQrCodeFlow
import ru.drrey.babyname.theme.api.ThemedBindingFragment
import ru.drrey.babyname.welcome.R
import ru.drrey.babyname.welcome.databinding.FragmentWelcomeBinding

class WelcomeFragment : ThemedBindingFragment<FragmentWelcomeBinding>() {

    override val viewBinder: (LayoutInflater) -> FragmentWelcomeBinding =
        { FragmentWelcomeBinding.inflate(it) }

    private val viewModel: WelcomeViewModel by sharedParentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.addPartnerView?.apply {
            text = getString(R.string.add_partner)
            setOnClickListener { activity?.router?.startFlow(AddPartnerFlow) }
        }
        viewBinding?.partnerQrCodeView?.apply {
            text = getString(R.string.partner_qr_code)
            setOnClickListener { activity?.router?.startFlow(PartnersQrCodeFlow) }
        }
        viewBinding?.partnerLaterView?.apply {
            text = getString(R.string.maybe_later)
            setOnClickListener { viewModel.onPartnersFinished() }
        }

        viewBinding?.girlSexView?.apply {
            text = getString(R.string.girl)
            setOnClickListener {
                viewModel.onSexSet(Sex.GIRL)
                themeViewModel.onAccentColorChange(R.color.pink)
            }
        }
        viewBinding?.boySexView?.apply {
            text = getString(R.string.boy)
            setOnClickListener {
                viewModel.onSexSet(Sex.BOY)
                themeViewModel.onAccentColorChange(R.color.blue)
            }
        }
        viewBinding?.noSexView?.apply {
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
            viewBinding?.textView?.apply {
                viewState.textResId?.let { text = getString(it) }
                visibility = View.VISIBLE
            }
        } else {
            viewBinding?.textView?.visibility = View.GONE
        }
        viewBinding?.partnerButtonsGroup?.visibility =
            if (viewState.partnerButtonsShown) View.VISIBLE else View.GONE
        viewBinding?.sexButtonsGroup?.visibility =
            if (viewState.sexButtonsShown) View.VISIBLE else View.GONE
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
