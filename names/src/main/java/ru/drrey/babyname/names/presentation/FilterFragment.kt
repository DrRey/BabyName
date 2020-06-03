package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_filter.*
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.theme.api.ThemedFragment

class FilterFragment : ThemedFragment() {

    companion object {
        fun newInstance() = FilterFragment()
    }

    private val viewModel: FilterViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNamesList().observe(viewLifecycleOwner, NonNullObserver {
            viewPager.apply {
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
            viewPager?.currentItem = viewState.currentNamePosition
            viewPager?.visibility = View.VISIBLE
        } else {
            viewPager?.visibility = View.GONE
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
