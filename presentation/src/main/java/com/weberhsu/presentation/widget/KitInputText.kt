package com.weberhsu.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.databinding.KitInputTextBinding
import androidx.core.content.withStyledAttributes


open class KitInputText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: KitInputTextBinding =
        KitInputTextBinding.inflate(LayoutInflater.from(context), this, true)

    // invoked when a valid input has been entered
    @JvmSynthetic
    var onInputSuccess: () -> Unit = {}

    // invoked when a input is not completed
    @JvmSynthetic
    var onInputNotCompleted: () -> Unit = {}

    // invoked when a input is invalid
    @JvmSynthetic
    var onInputError: () -> Unit = {}

    @JvmSynthetic
    var onAfterTextChanged: (Editable?) -> Unit = {}

    var textWatcherListener: TextWatcher? = null
    internal var isLastKeyDelete: Boolean = false
    private var mDeleteEmptyListener: DeleteEmptyListener? = null
    private var mContext: Context? = null
    private var judgeError: ((String) -> Boolean)? = null
    private var tipMsg: String = ""
    private var listenerAction: ((String) -> Unit)? = null

    private var allowCancelVisible = true

    var inputEnable: Boolean = true
        set(value) {
            field = value
            setInputIsEnable(value)
        }

    private var clearInputAction: ((View) -> Unit)? = null

    init {
        init(context, attrs)
        listenForDeleteEmpty()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        context.withStyledAttributes(attrs, R.styleable.kit_input_text) {
            var title = getString(R.styleable.kit_input_text_inputTitle) ?: ""
            var errorTip = getString(R.styleable.kit_input_text_inputErrorTip) ?: ""

            var showTitle = getBoolean(R.styleable.kit_input_text_showTitle, true)
            var showErrorTip = getBoolean(R.styleable.kit_input_text_showErrorTip, false)

            val hint = getString(R.styleable.kit_input_text_inputHint) ?: ""

            allowCancelVisible = getBoolean(R.styleable.kit_input_text_allowCancelVisible, true)

            if (hint.isNotEmpty()) {
                binding.etInputContent.hint = hint
            }
            binding.tvInputTitle.text = title
            binding.tvInputTip.text = errorTip
            tipMsg = errorTip

            setShowTitle(showTitle)
            setShowErrorTip(showErrorTip)
        }

        initView()
    }

    private fun resetInputColor() {
        if (binding.etInputContent.isEnabled) {
            binding.etInputContent.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.etInputContent.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.placeholder
                )
            )
        } else {
            binding.etInputContent.setTextColor(ContextCompat.getColor(context, R.color.disabled))
            binding.etInputContent.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.disabled
                )
            )
        }

    }

    private fun initView() {

        binding.ivInputCancel.setOnClickListener {
            binding.etInputContent.setText("")
            binding.ivInputCancel.visibility = View.GONE
            clearInputAction?.invoke(it)
        }

        binding.etInputContent.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textWatcherListener?.beforeTextChanged(p0, p1, p2, p3)
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textWatcherListener?.onTextChanged(p0, p1, p2, p3)
                }

                override fun afterTextChanged(p0: Editable?) {
                    onAfterTextChanged(p0)
                    textWatcherListener?.afterTextChanged(p0)
                    p0?.toString()?.let {
                        if (it.isNotEmpty() && inputEnable && allowCancelVisible) {
                            binding.ivInputCancel.visibility = View.VISIBLE
                        } else {
                            binding.ivInputCancel.visibility = View.GONE
                        }
                        judgeError?.run {
                            if (this(it)) {
                                showErrorStatus(tipMsg)
                            } else {
                                showNormalStatus()
                                listenerAction?.invoke(it)
                            }
                        } ?: listenerAction?.invoke(it)
                    }
                }
            }
        )

        binding.etInputContent.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.ivInputCancel.visibility = View.GONE
                if (binding.etInputContent.text.toString().isNotEmpty()) {
                    binding.etInputContent.setSelection(0)
                }
            } else {
                if (binding.etInputContent.text.toString()
                        .isNotEmpty() && binding.etInputContent.isEnabled && allowCancelVisible
                ) {
                    binding.ivInputCancel.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun listenForDeleteEmpty() {
        // This method works for hard keyboards and older phones.
        setOnKeyListener { _, keyCode, event ->
            isLastKeyDelete = isDeleteKey(keyCode, event)
            if (isLastKeyDelete && getEtInputContentView().length() == 0) {
                mDeleteEmptyListener?.onDeleteEmpty()
            }
            false
        }
    }

    private fun isDeleteKey(keyCode: Int, event: KeyEvent): Boolean {
        return keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN
    }

    fun setListenerAndTip(
        listenerAction: (String) -> Unit,
        judgeFun: ((String) -> Boolean)? = null,
        tip: String = ""
    ) {
        this.judgeError = judgeFun
        this.tipMsg = tip
        this.listenerAction = listenerAction
    }

    fun getInputText(): String {
        return binding.etInputContent.text?.toString()?.trim() ?: ""
    }

    fun setInputText(inputStr: String) {
        binding.etInputContent.setText(inputStr)
    }

    fun setTitle(title: String) {
        binding.tvInputTitle.text = title
    }

    private fun setTip(tip: String) {
        binding.tvInputTip.text = tip
    }

    private fun setInputIsEnable(enable: Boolean) {
        binding.etInputContent.isEnabled = enable
        mContext?.let {
            if (enable) {
                binding.etInputContent.setHintTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.placeholder
                    )
                )
                binding.etInputContent.setTextColor(ContextCompat.getColor(it, R.color.black))
            } else {
                binding.etInputContent.setHintTextColor(
                    ContextCompat.getColor(
                        it,
                        R.color.disabled
                    )
                )
                binding.etInputContent.setTextColor(ContextCompat.getColor(it, R.color.disabled))
            }
        }
        if (!enable) {
            binding.ivInputCancel.visibility = View.GONE
        }
    }

    fun setListener(listenerAction: (String) -> Unit) {
        this.listenerAction = listenerAction
    }

    fun showErrorStatus(tip: String) {
        setTip(tip)
    }

    /**
     * 正常状态显示
     */
    fun showNormalStatus() {
        setTip("")
    }

    fun setShowTitle(showTitle: Boolean) {
        if (showTitle) {
            binding.tvInputTitle.visibility = View.VISIBLE
        } else {
            binding.tvInputTitle.visibility = View.GONE
        }
    }

    fun setInputHint(hint: String) {
        binding.etInputContent.hint = hint
    }

    fun setShowErrorTip(showErrorTip: Boolean) {
        if (showErrorTip) {
            binding.clInputLayout.setBackgroundResource(R.drawable.bg_kit_input_text_error)
            binding.tvInputTip.visibility = View.VISIBLE
        } else {
            binding.clInputLayout.setBackgroundResource(R.drawable.bg_kit_input_text)
            binding.tvInputTip.visibility = View.GONE
        }
    }

    fun setClearInputClickListener(action: (View) -> Unit) {
        clearInputAction = action
    }

    fun setAllowCancelVisible(status: Boolean) {
        allowCancelVisible = status
    }

    fun getTitleView(): TextView {
        return binding.tvInputTitle
    }

    fun getErrorTipView(): TextView {
        return binding.tvInputTip
    }

    fun getEtInputContentView(): EditText {
        return binding.etInputContent
    }

    fun getCancelView(): ImageView {
        return binding.ivInputCancel
    }

    interface DeleteEmptyListener {
        fun onDeleteEmpty()
    }
}