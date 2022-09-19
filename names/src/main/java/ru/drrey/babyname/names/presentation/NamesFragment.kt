package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.domain.entity.Name
import ru.drrey.babyname.theme.api.ThemeViewState
import ru.drrey.babyname.theme.api.ThemedFragment

class NamesFragment : ThemedFragment() {

    companion object {
        fun newInstance() = NamesFragment()
    }

    private val viewModel: NamesViewModel by sharedParentViewModel()

    private val composeView by lazy { ComposeView(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return composeView.apply {
            setContent { NamesList(emptyList()) }
        }
    }

    @Composable
    private fun NamesList(names: List<Name>) {
        composeView.setContent {
            LazyColumn {
                names.map {
                    item {
                        NameRow(name = it)
                    }
                }
            }
        }
    }

    @Composable
    private fun NameRow(name: Name) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = name.displayName, fontSize = 16.sp)
            Row {
                Text(
                    text = "1", modifier = Modifier
                        .width(48.dp)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = "2", modifier = Modifier
                        .width(48.dp)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = "3", modifier = Modifier
                        .width(48.dp)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = "4", modifier = Modifier
                        .width(48.dp)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = "5", modifier = Modifier
                        .width(48.dp)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderState(it)
        })
        viewModel.getViewEvent().observe(viewLifecycleOwner, NonNullObserver {
            actOnEvent(it)
        })
        viewModel.loadNames()
    }

    override fun renderTheme(themeViewState: ThemeViewState) {
        composeView.invalidate()
    }

    private fun renderState(viewState: NamesViewState) {
        if (viewState.loadError != null) {
            composeView.setContent { NamesList(emptyList()) }
        } else {
            composeView.setContent { NamesList(viewState.names ?: emptyList()) }
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
