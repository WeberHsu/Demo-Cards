package com.weberhsu.presentation.ui.cards.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.presentation.R
import com.weberhsu.presentation.constant.KEY_CARD_DATA
import com.weberhsu.presentation.databinding.DialogEditCardCvvBinding
import com.weberhsu.presentation.databinding.DialogEditNormalBinding
import com.weberhsu.presentation.extensions.gone
import com.weberhsu.presentation.extensions.hideSoftKeyboard
import com.weberhsu.presentation.extensions.showSoftKeyboard
import com.weberhsu.presentation.extensions.visible
import com.weberhsu.presentation.ui.cards.widget.CardCvvInput
import com.weberhsu.presentation.widget.dialog.BaseCenterVerticalDialogFragment

class EditCardCvvDialogFragment : BaseCenterVerticalDialogFragment() {
    override var layoutResId: Int = 0
    private var binding: DialogEditCardCvvBinding? = null
    var listener: (String) -> Unit = {}

    companion object {
        fun newInstance(card: CardEntity, action: (String) -> Unit): EditCardCvvDialogFragment {
            return EditCardCvvDialogFragment().apply {
                listener = action
                arguments = Bundle().apply {
                    putParcelable(KEY_CARD_DATA, card)
                }
            }
        }
    }

    override fun createViewDelegate(): View? {
        binding = DialogEditCardCvvBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<CardEntity>(KEY_CARD_DATA)?.let {
            binding?.inputEdit?.run {
                setInputText(it.cvv)
                onAfterTextChanged = {
                    checkCvvFieldValid(this)
                }
                getEtInputContentView().setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                        if (checkCvvFieldValid(this)) {
                            listener.invoke(getInputText())
                            hideSoftKeyboard()
                            dismissAllowingStateLoss()
                        }
                        true
                    } else {
                        false
                    }
                }
                getEtInputContentView().showSoftKeyboard()
            }
        }
    }

    override fun work(savedInstanceState: Bundle?) {

    }

    private fun checkCvvFieldValid(input: CardCvvInput): Boolean {
        if (input.getInputText().isEmpty() || !input.isValid) {
            input.setShowErrorTip(true)
            if (!input.isValid && input.getInputText().isNotEmpty()) {
                input.showErrorStatus("The field is invalid")
            } else {
                input.showErrorStatus(getString(R.string.field_required))
            }
            return false
        } else {
            input.setShowErrorTip(false)
            return true
        }
    }
}