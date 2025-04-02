package com.weberhsu.presentation.ui.cards.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.weberhsu.domain.entity.CardEntity
import com.weberhsu.presentation.R
import com.weberhsu.presentation.constant.KEY_CARD_DATA
import com.weberhsu.presentation.databinding.DialogEditNormalBinding
import com.weberhsu.presentation.extensions.hideSoftKeyboard
import com.weberhsu.presentation.extensions.showSoftKeyboard
import com.weberhsu.presentation.widget.KitInputText
import com.weberhsu.presentation.widget.dialog.BaseCenterVerticalDialogFragment

class EditCardFieldDialogFragment : BaseCenterVerticalDialogFragment() {
    override var layoutResId: Int = 0
    private var binding: DialogEditNormalBinding? = null
    var listener: (String) -> Unit = {}

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CONTENT = "KEY_CONTENT"

        fun newInstance(
            card: CardEntity,
            title: String,
            content: String,
            action: (String) -> Unit
        ): EditCardFieldDialogFragment {
            return EditCardFieldDialogFragment().apply {
                listener = action
                arguments = Bundle().apply {
                    putParcelable(KEY_CARD_DATA, card)
                    putString(KEY_TITLE, title)
                    putString(KEY_CONTENT, content)
                }
            }
        }
    }

    override fun createViewDelegate(): View? {
        binding = DialogEditNormalBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun setUpViews(root: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<CardEntity>(KEY_CARD_DATA)?.let {
            binding?.inputEdit?.run {
                setTitle(arguments?.getString(KEY_TITLE) ?: "")
                setInputText(arguments?.getString(KEY_CONTENT) ?: "")
                onAfterTextChanged = {
                    checkFieldValid(this)
                }
                getEtInputContentView().setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                    ) {
                        if (checkFieldValid(this)) {
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

    private fun checkFieldValid(input: KitInputText): Boolean {
        if (input.getInputText().isEmpty()) {
            input.setShowErrorTip(true)
            input.showErrorStatus(getString(R.string.field_required))
            return false
        } else {
            input.setShowErrorTip(false)
            return true
        }
    }
}