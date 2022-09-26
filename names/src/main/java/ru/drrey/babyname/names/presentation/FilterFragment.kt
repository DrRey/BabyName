package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.databinding.FragmentFilterBinding
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.theme.api.ThemedBindingFragment

class FilterFragment : ThemedBindingFragment<FragmentFilterBinding>() {

    companion object {
        fun newInstance() = FilterFragment()
    }

    override val viewBinder: (LayoutInflater) -> FragmentFilterBinding =
        { FragmentFilterBinding.inflate(it) }

    private val viewModel: FilterViewModel by sharedParentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNamesList().observe(viewLifecycleOwner, NonNullObserver {
            viewBinding?.viewPager?.apply {
                adapter =
                    FilterPagerAdapter(it, viewModel.getNamesMap()) { name: Name, allow: Boolean ->
                        viewModel.onNameFiltered(name, allow)
                    }
            }
        })
        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.loadNames()
    }

    private fun renderState(viewState: FilterViewState) {
        if (viewState.currentNamePosition != null) {
            viewBinding?.viewPager?.currentItem = viewState.currentNamePosition
            viewBinding?.viewPager?.visibility = View.VISIBLE
        } else {
            viewBinding?.viewPager?.visibility = View.GONE
        }
    }

    private fun actOnEvent(viewEvent: FilterViewEvent) {
        when (viewEvent) {
            FilterViewEvent.NoNamesLeft -> {
                router?.backTo(null)
            }
            is FilterViewEvent.Error -> {
                Toast.makeText(context, viewEvent.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
