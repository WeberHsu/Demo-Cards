package com.weberhsu.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.weberhsu.presentation.R
import com.weberhsu.presentation.util.SystemUIController

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    abstract val bindLayout: (LayoutInflater) -> VB

    protected val binding: VB
        get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindLayout.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        prepareView(savedInstanceState)

        compactStatusBar()
    }

    abstract fun prepareView(savedInstanceState: Bundle?)

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    open fun compactStatusBar() {
        @ColorRes
        fun Int.getColor() = ContextCompat.getColor(this@BaseActivity, this)

        SystemUIController.apply {
            layoutToCutOffArea(window, true)
            compatStatusBar(window, getStatusBarColor().getColor())
            compatNavigationBar(window, getNavigationBarBackground().getColor())
        }
    }

    @ColorRes
    open fun getStatusBarColor(): Int {
        return R.color.main_color
    }

    @ColorRes
    open fun getNavigationBarBackground(): Int = getStatusBarColor()
}