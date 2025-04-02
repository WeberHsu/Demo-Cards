package com.weberhsu.presentation.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.weberhsu.presentation.R

abstract class BaseCenterVerticalDialogFragment: BaseDialogFragment() {

    override var noTitle: Boolean = true
    override var backgroundColorResId: Int = android.R.color.transparent

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext!!, R.style.CardsDialog)
        dialog.setCanceledOnTouchOutside(true)
        val lp = dialog.window?.attributes
        lp?.gravity = Gravity.CENTER_VERTICAL
        dialog.window?.attributes = lp
        dialog.window?.setWindowAnimations(R.style.CardsBottomDialog)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }
}