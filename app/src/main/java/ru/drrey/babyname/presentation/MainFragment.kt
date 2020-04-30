package ru.drrey.babyname.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.navigation.*

class MainFragment : Fragment() {

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
        selectSexFilterButton(viewState.sexFilter)
    }

    private fun selectSexFilterButton(sexFilter: Sex?) {
        girlSexView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (sexFilter == Sex.GIRL) R.color.colorAccent else R.color.transparent
            )
        )
        boySexView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (sexFilter == Sex.BOY) R.color.colorAccent else R.color.transparent
            )
        )
        allSexView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (sexFilter == null) R.color.colorAccent else R.color.transparent
            )
        )
    }

    private fun processEvent(viewEvent: MainViewEvent) {
        if (viewEvent == MainViewEvent.WelcomeScreenNeeded) {
            activity?.router?.startFlow(WelcomeFlow)
        }
    }
}
