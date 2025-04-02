package com.weberhsu.presentation.widget.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment : DialogFragment() {

    abstract var layoutResId: Int

    protected var mContext: Context? = null

    /**
     * This is an entry point for View Binding to instantiate and return the root view for SimpleDialogFragment.
     */
    open fun createViewDelegate(): View? = null

    abstract var noTitle: Boolean

    abstract var backgroundColorResId: Int

    abstract fun setUpViews(root: View, savedInstanceState: Bundle?)

    abstract fun work(savedInstanceState: Bundle?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (noTitle) {
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        dialog?.window?.setBackgroundDrawableResource(backgroundColorResId)
        return try {
            createViewDelegate() ?: inflater.inflate(layoutResId, container, false)
        } catch (e: Exception) {
            try {
                LayoutInflater.from(activity?.applicationContext)
                    .inflate(layoutResId, container, false)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commitAllowingStateLoss()
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(view, savedInstanceState)
    }

    @Suppress("deprecation")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (view != null) {
            work(savedInstanceState)
        }
    }
}