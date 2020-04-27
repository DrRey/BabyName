package ru.drrey.babyname.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.navigation.AddPartnerFlow
import ru.drrey.babyname.navigation.AuthFlow
import ru.drrey.babyname.navigation.NamesFlow
import ru.drrey.babyname.navigation.PartnersQrCodeFlow

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.loadData()
        }
    }

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
        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
    }

    private fun renderState(viewState: MainViewState) {
        if (!viewState.isLoading) {
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
        }
    }
}
