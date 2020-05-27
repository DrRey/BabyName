package ru.drrey.babyname.theme.api

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.common.presentation.base.NonNullObserver

abstract class ThemedFragment : Fragment() {

    protected val themeViewModel: ThemeViewModelApi by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeViewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderTheme(it)
        })
    }

    abstract fun renderTheme(themeViewState: ThemeViewState)
}