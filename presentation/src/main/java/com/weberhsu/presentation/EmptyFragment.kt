package com.weberhsu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.weberhsu.presentation.base.BaseFragment
import com.weberhsu.presentation.databinding.FragmentEmptyBinding

class EmptyFragment: BaseFragment<FragmentEmptyBinding>() {
    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEmptyBinding
        get() = FragmentEmptyBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {

    }
}