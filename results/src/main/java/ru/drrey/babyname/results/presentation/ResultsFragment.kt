package ru.drrey.babyname.results.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.results_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.results.R

@ExperimentalCoroutinesApi
class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    private val resultsAdapter = GroupAdapter<ViewHolder>()
    private val viewModel: ResultsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.results_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = resultsAdapter
            addItemDecoration(VerticalSpaceDivider(context))
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                ResultsNotLoaded -> {

                }
                ResultsLoading -> {

                }
                is ResultsLoaded -> {
                    resultsAdapter.clear()
                    it.results.forEach { result ->
                        resultsAdapter.add(ResultItem(result))
                    }
                }
                is ResultsLoadError -> {
                    resultsAdapter.clear()
                }
            }
        })
        viewModel.loadResults()
    }
}
