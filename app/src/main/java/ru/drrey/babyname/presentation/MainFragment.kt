package ru.drrey.babyname.presentation

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.databinding.FragmentMainBinding
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.navigation.*
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedBindingFragment

class MainFragment : ThemedBindingFragment<FragmentMainBinding>() {

    override val viewBinder: (LayoutInflater) -> FragmentMainBinding =
        { FragmentMainBinding.inflate(it) }

    private val viewModel: MainViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinder(inflater).let {
            viewBinding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.resultsView?.apply {
            text = getString(R.string.results)
            setOnClickListener { activity?.router?.startFlow(ResultsFlow) }
        }
        viewBinding?.addPartnerView?.apply {
            text = getString(R.string.add_partner)
            setOnClickListener { activity?.router?.startFlow(AddPartnerFlow) }
        }
        viewBinding?.partnerQrCodeView?.apply {
            text = getString(R.string.show_partner_qr)
            setOnClickListener { activity?.router?.startFlow(PartnersQrCodeFlow) }
        }
        viewBinding?.girlSexView?.setOnClickListener { viewModel.onSexSet(Sex.GIRL) }
        viewBinding?.boySexView?.setOnClickListener { viewModel.onSexSet(Sex.BOY) }
        viewBinding?.allSexView?.setOnClickListener { viewModel.onSexSet(null) }

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
        themeViewState.accentColorResId?.let { accentColorResId ->
            viewBinding?.progressBar?.indeterminateDrawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), accentColorResId),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun renderState(viewState: MainViewState) {
        if (!viewState.showOverlay) {
            viewBinding?.overlayView?.visibility = View.GONE
        }
        if (viewState.error != null) {
            Toast.makeText(context, viewState.error, Toast.LENGTH_LONG).show()
        } else {
            if (viewState.isLoggedIn) {
                viewBinding?.authView?.visibility = View.GONE
            } else {
                viewBinding?.authView?.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.login)
                    setOnClickListener { activity?.router?.startFlow(AuthFlow) }
                }
            }
            viewBinding?.partnersView?.text = getString(R.string.partners, viewState.partnersCount)
            viewBinding?.namesView?.apply {
                visibility = if (viewState.isLoadingData) View.INVISIBLE else View.VISIBLE
                if (viewState.unfilteredNamesCount > 0) {
                    text = getString(R.string.names_unfiltered, viewState.unfilteredNamesCount)
                    setOnClickListener { activity?.router?.startFlow(FilterFlow) }
                } else {
                    text = getString(R.string.names_starred, viewState.starredNamesCount)
                    setOnClickListener { activity?.router?.startFlow(NamesFlow) }
                }
            }
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
            viewBinding?.girlSexView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (sexFilter == Sex.GIRL) accentColorResId else R.color.transparent
                )
            )
            viewBinding?.boySexView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (sexFilter == Sex.BOY) accentColorResId else R.color.transparent
                )
            )
            viewBinding?.allSexView?.setBackgroundColor(
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

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}
