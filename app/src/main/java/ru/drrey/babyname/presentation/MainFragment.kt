package ru.drrey.babyname.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.navigation.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authButton.setOnClickListener { activity?.router?.startFlow(AuthFlow) }
        addPartnerButton.setOnClickListener { activity?.router?.startFlow(AddPartnerFlow) }
        clearPartnersButton.setOnClickListener { viewModel.onClearPartners() }
        viewPartnersQrCodeButton.setOnClickListener { activity?.router?.startFlow(PartnersQrCodeFlow) }
        namesButton.setOnClickListener { activity?.router?.startFlow(NamesFlow) }
        resultsButton.setOnClickListener { activity?.router?.startFlow(ResultsFlow) }

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        if (savedInstanceState == null) {
            viewModel.loadData()
        }
    }

    private fun renderState(viewState: MainViewState) {
        userIdView.text = viewState.userId ?: ""
        partnersIdView.text = viewState.partnerIds?.joinToString(separator = "\n") ?: ""
        namesCountView.text = viewState.starredNamesCount?.toString() ?: ""
    }
}
