package ru.drrey.babyname.theme.api

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.drrey.babyname.common.presentation.base.NonNullObserver
import ru.drrey.babyname.common.presentation.base.ViewBindingFragment

abstract class ThemedBindingFragment<VB: ViewBinding> : ViewBindingFragment<VB>() {

    protected val themeViewModel: ThemeViewModelApi by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeViewModel.getViewState().observe(viewLifecycleOwner, NonNullObserver {
            renderTheme(it)
        })
    }

    open fun renderTheme(themeViewState: ThemeViewState) {}
}