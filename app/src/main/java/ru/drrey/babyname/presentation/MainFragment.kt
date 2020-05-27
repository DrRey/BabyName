package ru.drrey.babyname.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_main.*
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.navigation.*
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedFragment

class MainFragment : ThemedFragment() {

    private val viewModel: MainViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPartnerView?.apply {
            text = getString(R.string.add_partner)
            setOnClickListener { activity?.router?.startFlow(AddPartnerFlow) }
        }
        partnerQrCodeView?.apply {
            text = getString(R.string.show_partner_qr)
            setOnClickListener { activity?.router?.startFlow(PartnersQrCodeFlow) }
        }
        namesView?.setOnClickListener { activity?.router?.startFlow(NamesFlow) }
        girlSexView?.setOnClickListener { viewModel.onSexSet(Sex.GIRL) }
        boySexView?.setOnClickListener { viewModel.onSexSet(Sex.BOY) }
        allSexView?.setOnClickListener { viewModel.onSexSet(null) }

        themeViewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderTheme(it)
        })
        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            processEvent(it)
        })

        if (savedInstanceState == null) {
            viewModel.loadData()
        }
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        viewModel.invalidateViewState()
    }

    private fun renderState(viewState: MainViewState) {
        if (!viewState.showOverlay) {
            overlayView?.visibility = View.GONE
        }
        if (viewState.error != null) {
            Toast.makeText(context, viewState.error, Toast.LENGTH_LONG).show()
        } else {
            if (viewState.isLoggedIn) {
                authView?.apply {
                    text = getString(R.string.logged_in)
                    setOnClickListener(null)
                }
            } else {
                authView?.apply {
                    text = getString(R.string.login)
                    setOnClickListener { activity?.router?.startFlow(AuthFlow) }
                }
            }
            partnersView?.text = getString(R.string.partners, viewState.partnersCount)
            namesView?.text = getString(R.string.names_starred, viewState.starredNamesCount)
        }
        if (viewState.sexFilterLoaded) {
            selectSexFilterButton(viewState.sexFilter)
            when (viewState.sexFilter) {
                Sex.BOY -> {
                    themeViewModel.onAccentColorChange(R.color.blue)
                }
                Sex.GIRL -> {
                    themeViewModel.onAccentColorChange(R.color.pink)
                }
                null -> {
                    themeViewModel.onAccentColorChange(R.color.green)
                }
            }
        }
    }

    private fun selectSexFilterButton(sexFilter: Sex?) {
        themeViewModel.accentColorResId?.let { accentColorResId ->
            girlSexView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (sexFilter == Sex.GIRL) accentColorResId else R.color.transparent
                )
            )
            boySexView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (sexFilter == Sex.BOY) accentColorResId else R.color.transparent
                )
            )
            allSexView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (sexFilter == null) accentColorResId else R.color.transparent
                )
            )
        }
    }

    private fun processEvent(viewEvent: MainViewEvent) {
        if (viewEvent == MainViewEvent.WelcomeScreenNeeded) {
            activity?.router?.startFlow(WelcomeFlow)
        }
    }
}
