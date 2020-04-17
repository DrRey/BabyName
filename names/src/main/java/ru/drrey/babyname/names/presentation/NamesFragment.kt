package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.names_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.names.R

@ExperimentalCoroutinesApi
class NamesFragment : Fragment() {

    companion object {
        fun newInstance() = NamesFragment()
    }

    private val namesAdapter = GroupAdapter<ViewHolder>()
    private val namesSection = Section()
    private val viewModel: NamesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.names_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = namesAdapter
            addItemDecoration(VerticalSpaceDivider(context))
        }
        namesAdapter.add(namesSection)

        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.loadNames()
    }

    private fun renderState(viewState: NamesViewState) {
        if (viewState.loadError != null) {
            namesSection.update(emptyList())
        } else {
            viewState.names?.let {
                namesSection.update(it.map { name ->
                    NameItem(name) { starredName, position, stars ->
                        viewModel.setStars(starredName, position, stars)
                    }
                })
            }
        }
    }

    private fun actOnEvent(viewEvent: NamesViewEvent) {
        when (viewEvent) {
            is NamesViewEvent.StarsSetError -> {
                Toast.makeText(context, viewEvent.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
