package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_names.*
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.R
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedFragment

class NamesFragment : ThemedFragment() {

    companion object {
        fun newInstance() = NamesFragment()
    }

    private val namesAdapter = GroupAdapter<ViewHolder>()
    private val namesSection = Section()
    private val viewModel: NamesViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = namesAdapter
            addItemDecoration(VerticalSpaceDivider(context))
        }
        namesAdapter.add(namesSection)

        themeViewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderTheme(it)
        })
        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.loadNames()
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        namesSection.notifyChanged()
    }

    private fun renderState(viewState: NamesViewState) {
        if (viewState.loadError != null) {
            namesSection.update(emptyList())
        } else {
            viewState.names?.let {
                namesSection.update(it.map { name ->
                    NameItem(name, themeViewModel) { starredName, position, stars ->
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
