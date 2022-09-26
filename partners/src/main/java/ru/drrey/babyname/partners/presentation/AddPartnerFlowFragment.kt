package ru.drrey.babyname.partners.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.base.FlowFragment
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.partners.di.PartnersComponent
import ru.drrey.babyname.partners.navigation.AddPartnerFragmentScreen

class AddPartnerFlowFragment : FlowFragment() {
    override val featureDependencies = listOf(PartnersComponent::class)

    private val viewModel: PartnersViewModel by viewModel()

    override fun getFlowScreen() = AddPartnerFragmentScreen()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            when (it) {
                is PartnersViewEvent.PartnerAddError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                PartnersViewEvent.PartnerAdded -> {
                    router.exit()
                }
            }
        })
    }
}