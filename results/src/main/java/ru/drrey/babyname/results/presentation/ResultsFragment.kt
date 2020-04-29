package ru.drrey.babyname.results.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_results.*
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.results.R

class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    private val resultsAdapter = GroupAdapter<ViewHolder>()
    private val resultsSection = Section()
    private val viewModel: ResultsViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView?.apply {
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
                resultsSection.update(it.map { result -> ResultItem(result) })
            }
        }
    }
}
