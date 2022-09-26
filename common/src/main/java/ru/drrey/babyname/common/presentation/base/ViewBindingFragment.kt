package ru.drrey.babyname.common.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    protected abstract val viewBinder: ((LayoutInflater) -> VB)
    protected var viewBinding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return viewBinder(inflater).let {
            viewBinding = it
            it.root
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}