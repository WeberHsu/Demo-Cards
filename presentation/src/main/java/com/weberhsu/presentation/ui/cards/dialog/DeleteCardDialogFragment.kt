package com.weberhsu.presentation.ui.cards.dialog

import android.os.Bundle
import android.view.View
import com.weberhsu.presentation.databinding.DialogDeleteCardBinding
import com.weberhsu.presentation.extensions.clickWithTrigger
import com.weberhsu.presentation.widget.dialog.BaseCenterVerticalDialogFragment

class DeleteCardDialogFragment : BaseCenterVerticalDialogFragment() {
    override var layoutResId: Int = 0
    private var binding: DialogDeleteCardBinding? = null
    var listener: () -> Unit = {}

    companion object {
        fun newInstance(action: () -> Unit): DeleteCardDialogFragment {
            return DeleteCardDialogFragment().apply {
                listener = action
            }
        }
    }

    override fun createViewDelegate(): View? {
        binding = DialogDeleteCardBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        binding?.btnConfirm?.clickWithTrigger {
            listener.invoke()
            dismissAllowingStateLoss()
        }
        binding?.btnCancel?.clickWithTrigger {
            dismissAllowingStateLoss()
        }
    }

    override fun work(savedInstanceState: Bundle?) {

    }
}