package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_filter.*
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.router
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.R
import ru.drrey.babyname.names.api.Sex
import ru.drrey.babyname.theme.api.ThemeViewState
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

        yesView?.apply {
            text = getString(R.string.yes)
            setOnClickListener { viewModel.onNameFiltered(true) }
        }
        noView?.apply {
            text = getString(R.string.no)
            setOnClickListener { viewModel.onNameFiltered(false) }
        }

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.loadNames()
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        themeViewState.accentColorResId?.let {
        }
    }

    private fun renderState(viewState: FilterViewState) {
        if (viewState.currentName != null) {
            textView?.apply {
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (viewState.currentName.sex == Sex.BOY) R.color.blue else R.color.pink
                    )
                )
                text = viewState.currentName.displayName
            }
            contentLayout?.visibility = View.VISIBLE
        } else {
            contentLayout?.visibility = View.GONE
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
