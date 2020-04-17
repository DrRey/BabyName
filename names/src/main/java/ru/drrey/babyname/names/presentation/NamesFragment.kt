package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.names_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.drrey.babyname.common.presentation.VerticalSpaceDivider
import ru.drrey.babyname.names.R

@ExperimentalCoroutinesApi
class NamesFragment : Fragment() {

    companion object {
        fun newInstance() = NamesFragment()
    }

    private val namesAdapter = GroupAdapter<ViewHolder>()
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

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                NamesNotLoaded -> {

                }
                NamesLoading -> {

                }
                is NamesLoaded -> {
                    namesAdapter.clear()
                    it.names.forEach { name ->
                        namesAdapter.add(NameItem(name) { starredName, position, stars ->
                            viewModel.setStars(starredName, position, stars)
                        })
                    }
                }
                is NamesLoadError -> {
                    namesAdapter.clear()
                }
                is SetStarsError -> {
                    (namesAdapter.getItem(it.position) as? NameItem)?.name?.stars = null
                    namesAdapter.notifyItemChanged(it.position)
                }
                is SetStarsSuccess -> {
                    (namesAdapter.getItem(it.position) as? NameItem)?.name?.stars = it.stars
                    namesAdapter.notifyItemChanged(it.position)
                }
            }
        })
        viewModel.loadNames()
    }
}
