package ru.drrey.babyname.presentation

import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.FeatureStarter
import ru.drrey.babyname.R
import ru.drrey.babyname.common.presentation.base.BaseActivity
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.navigation.*
import ru.terrakok.cicerone.Navigator

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModel()

    override val navigator: Navigator
        get() = AppNavigator(this, R.id.fragmentHolder)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        FeatureStarter.initMainFeature()

        authButton.setOnClickListener { (router as? AppRouter)?.startFlow(AuthFlow) }
        addPartnerButton.setOnClickListener { (router as? AppRouter)?.startFlow(AddPartnerFlow) }
        clearPartnersButton.setOnClickListener { viewModel.onClearPartners() }
        viewPartnersQrCodeButton.setOnClickListener {
            (router as? AppRouter)?.startFlow(
                PartnersQrCodeFlow
            )
        }
        namesButton.setOnClickListener { (router as? AppRouter)?.startFlow(NamesFlow) }
        resultsButton.setOnClickListener { (router as? AppRouter)?.startFlow(ResultsFlow) }

        viewModel.getViewState().observe(this, NonNullObserver {
            renderState(it)
        })
    }

    private fun renderState(viewState: MainViewState) {
        userIdView.text = viewState.userId ?: ""
        partnersIdView.text = viewState.partnerIds?.joinToString(separator = "\n") ?: ""
        namesCountView.text = viewState.starredNamesCount?.toString() ?: ""
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}
