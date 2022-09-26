package ru.drrey.babyname.names.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.names.R
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
        return composeView
    }

    @Composable
    private fun NamesList(names: List<Name>) {
        LazyColumn {
            names.mapIndexed { index, name ->
                item {
                    NameRow(name, index)
                }
            }
        }
    }

    @Composable
    private fun NameRow(name: Name, position: Int) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(48.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name.displayName,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                nameRateButton(name, position, 1)
                nameRateButton(name, position, 2)
                nameRateButton(name, position, 3)
                nameRateButton(name, position, 4)
                nameRateButton(name, position, 5)
            }
        }
    }

    @Composable
    private fun nameRateButton(name: Name, position: Int, value: Int) {
        val activeColor = colorResource(themeViewModel.accentColorResId ?: R.color.transparent)
        val inactiveColor = colorResource(R.color.grey)
        val backgroundColor = if (name.stars == value) activeColor else inactiveColor
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp)
                .width(48.dp)
                .background(backgroundColor)
                .clickable {
                    viewModel.setStars(name, position, value)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value.toString())
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
