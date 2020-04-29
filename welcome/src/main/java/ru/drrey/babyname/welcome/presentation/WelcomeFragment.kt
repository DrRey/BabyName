package ru.drrey.babyname.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.drrey.babyname.common.presentation.sharedParentViewModel
import ru.drrey.babyname.welcome.R

class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by sharedParentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }
}
