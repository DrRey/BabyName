package ru.drrey.babyname.results.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.results.databinding.FragmentResultsBinding
import ru.drrey.babyname.theme.api.ThemedBindingFragment

class ResultsFragment : ThemedBindingFragment<FragmentResultsBinding>() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    override val viewBinder: (LayoutInflater) -> FragmentResultsBinding =
        { FragmentResultsBinding.inflate(it) }

    private val resultsAdapter = GroupieAdapter()
    private val resultsSection = Section()
    private val viewModel: ResultsViewModel by sharedParentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = resultsAdapter
            addItemDecoration(VerticalSpaceDivider(context))
        }
        resultsAdapter.add(resultsSection)

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.loadResults()
    }

    private fun renderState(viewState: ResultsViewState) {
        if (viewState.error != null) {
            resultsSection.update(emptyList())
        } else {
            viewState.results?.let {
                resultsSection.update(it.map { result ->
                    ResultItem(
                        result,
                        themeViewModel.accentColorResId
                    )
                })
            }
        }
    }
}
