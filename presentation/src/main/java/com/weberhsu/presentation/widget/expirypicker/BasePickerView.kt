package com.weberhsu.presentation.widget.expirypicker

import android.view.ViewGroup

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.weberhsu.presentation.R
import com.weberhsu.presentation.widget.expirypicker.listener.OnDismissListener

abstract class BasePickerView(private val context: Context) {

    var contentContainer: ViewGroup? = null
    var mPickerOptions: PickerOptions? = null
    private var rootView: ViewGroup? = null // root view of detached view
    private var dialogView: ViewGroup? = null // root view of detached dialog
    private var onDismissListener: OnDismissListener? = null
    private var dismissing = false
    private var outAnim: Animation? = null
    private var inAnim: Animation? = null
    private var isShowing = false
    private var animGravity: Int = Gravity.BOTTOM
    private var mDialog: Dialog? = null
    private var clickView: View? = null
    private var isAnim = true

    // cancel callback
    @JvmSynthetic
    protected var onCancel: () -> Unit = {}

    // confirm callback
    @JvmSynthetic
    protected var onConfirm: () -> Unit = {}

    /**
     * Setup pickerview content
     */
    abstract fun initContentView()

    // TODO add string for cancel & confirm
    protected fun initViews() {
        initTextColors()

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = mPickerOptions?.dialogGravity ?: Gravity.BOTTOM
        val layoutInflater = LayoutInflater.from(context)

        if (isDialog()) {
            dialogView = layoutInflater.inflate(R.layout.pickerview_base, null, false) as ViewGroup?
            dialogView?.let {
                dialogView!!.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                // root viewgroup
                contentContainer =
                    dialogView!!.findViewById<View>(R.id.contentContainer) as ViewGroup
                contentContainer!!.layoutParams = params
                (dialogView?.findViewById(R.id.tvCancel) as TextView?)?.setOnClickListener {
                    dismiss()
                    onCancel()
                }

                (dialogView?.findViewById(R.id.tvConfirm) as TextView?)?.setOnClickListener {
                    onConfirm()
                    dismiss()
                }
                createDialog()
                dialogView!!.setOnClickListener { dismiss() }
            }
        } else {
            // decorView is activity's root view, contains contentView and titleView
            if (mPickerOptions?.decorView == null) {
                mPickerOptions?.decorView = (context as Activity).window.decorView as ViewGroup
            }
            // add components to decorView
            rootView =
                layoutInflater.inflate(
                    R.layout.pickerview_base,
                    mPickerOptions?.decorView,
                    false
                ) as ViewGroup?
            rootView!!.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            if (mPickerOptions?.outSideColor !== -1 && mPickerOptions != null) {
                rootView!!.setBackgroundColor(mPickerOptions!!.outSideColor)
            }

            // root viewgroup
            contentContainer = rootView!!.findViewById<View>(R.id.contentContainer) as ViewGroup
            contentContainer!!.layoutParams = params
        }
        setKeyBackCancelable(true)

        // cancel color
        mPickerOptions?.textColorCancel?.let {
            (dialogView?.findViewById(R.id.tvCancel) as TextView?)?.setTextColor(mPickerOptions!!.textColorCancel!!)
        }
        // confirm color
        mPickerOptions?.textColorConfirm?.let {
            (dialogView?.findViewById(R.id.tvConfirm) as TextView?)?.setTextColor(mPickerOptions!!.textColorConfirm!!)
        }
        // title text
        mPickerOptions?.textContentTitle?.let {
            (dialogView?.findViewById(R.id.tvTitle) as TextView?)?.visibility = View.VISIBLE
            (dialogView?.findViewById(R.id.tvTitle) as TextView?)?.text =
                mPickerOptions?.textContentTitle
        }
        // cancel text
        mPickerOptions?.textContentCancel?.let {
            (dialogView?.findViewById(R.id.tvCancel) as TextView?)?.text =
                mPickerOptions?.textContentCancel
        }
        // confirm text
        mPickerOptions?.textContentConfirm?.let {
            (dialogView?.findViewById(R.id.tvConfirm) as TextView?)?.text =
                mPickerOptions?.textContentConfirm
        }

    }

    protected fun initAnim() {
        inAnim = getInAnimation()
        outAnim = getOutAnimation()
    }

    protected fun initCommonOptions(view: com.weberhsu.presentation.widget.expirypicker.wheelview.view.KitWheelView) {
        view.setItemsVisibleCount(mPickerOptions!!.itemsVisibleCount)
        view.setAlphaGradient(mPickerOptions!!.isAlphaGradient)
        view.setCyclic(mPickerOptions!!.cyclic)
        view.setDividerColor(mPickerOptions!!.dividerColor)
        view.setDividerType(mPickerOptions!!.dividerType)
        view.setLineSpacingMultiplier(mPickerOptions!!.lineSpacingMultiplier)
        view.setTextColorOut(mPickerOptions!!.textColorOut)
        view.setTextColorCenter(mPickerOptions!!.textColorCenter)
    }

    /**
     * @param v      (popup from which view)
     * @param isAnim is show animation
     */
    fun show(v: View?, isAnim: Boolean) {
        clickView = v
        this.isAnim = isAnim
        show()
    }

    fun show(isAnim: Boolean) {
        show(null, isAnim)
    }

    fun show(v: View?) {
        clickView = v
        show()
    }

    /**
     * add view to root view
     */
    fun show() {
        if (isDialog()) {
            showDialog()
        } else {
            if (isShowing()) {
                return
            }
            isShowing = true
            onAttached(rootView)
            rootView!!.requestFocus()
        }
    }

    /**
     * call when show()
     *
     * @param view
     */
    private fun onAttached(view: View?) {
        mPickerOptions?.decorView?.addView(view)
        if (isAnim) {
            contentContainer!!.startAnimation(inAnim)
        }
    }

    fun isShowing(): Boolean {
        return if (isDialog()) {
            false
        } else {
            rootView!!.parent != null || isShowing
        }
    }

    fun dismiss() {
        if (isDialog()) {
            dismissDialog()
        } else {
            if (dismissing) {
                return
            }
            if (isAnim) {
                //disappear animation
                outAnim?.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        dismissImmediately()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                contentContainer!!.startAnimation(outAnim)
            } else {
                dismissImmediately()
            }
            dismissing = true
        }
    }

    fun dismissImmediately() {
        mPickerOptions?.decorView?.post {
            // remove from root view
            mPickerOptions?.decorView?.removeView(rootView)
            isShowing = false
            dismissing = false
            if (onDismissListener != null) {
                onDismissListener!!.onDismiss(this@BasePickerView)
            }
        }
    }

    private fun getInAnimation(): Animation {
        val res: Int = getAnimationResource(animGravity, true)
        return AnimationUtils.loadAnimation(context, res)
    }

    private fun getOutAnimation(): Animation {
        val res: Int = getAnimationResource(animGravity, false)
        return AnimationUtils.loadAnimation(context, res)
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener): BasePickerView {
        this.onDismissListener = onDismissListener
        return this
    }

    private fun setKeyBackCancelable(isCancelable: Boolean) {
        val view: ViewGroup? = if (isDialog()) {
            dialogView
        } else {
            rootView
        }
        view!!.isFocusable = isCancelable
        view.isFocusableInTouchMode = isCancelable
        if (isCancelable) {
            view.setOnKeyListener(onKeyBackListener)
        } else {
            view.setOnKeyListener(null)
        }
    }

    private val onKeyBackListener: View.OnKeyListener = object : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action === MotionEvent.ACTION_DOWN && isShowing()) {
                dismiss()
                return true
            }
            return false
        }
    }

    protected fun setOutSideCancelable(isCancelable: Boolean): BasePickerView {
        if (rootView != null) {
            val view: View = rootView!!.findViewById(R.id.rootContainer)
            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener)
            } else {
                view.setOnTouchListener(null)
            }
        }
        return this
    }

    fun setDialogOutSideCancelable() {
        if (mDialog != null && mPickerOptions != null) {
            mDialog!!.setCancelable(mPickerOptions!!.cancelable)
        }
    }

    /**
     * Called when the user touch on black overlay, in order to dismiss the dialog.
     */
    private val onCancelableTouchListener: View.OnTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            dismiss()
        }
        false
    }

    fun findContentChildViewById(id: Int): View? {
        return contentContainer?.findViewById(id)
    }

    private fun createDialog() {
        dialogView?.let {
            mDialog = Dialog(context, R.style.pickerview_dialog)
            mDialog!!.setCancelable(mPickerOptions?.cancelable ?: true) //不能点外面取消,也不能点back取消
            mDialog!!.setContentView(it)
            val dialogWindow: Window? = mDialog!!.window

            dialogWindow?.let {
                val lp: WindowManager.LayoutParams = dialogWindow.attributes
                lp.gravity = mPickerOptions?.dialogGravity ?: Gravity.BOTTOM
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialogWindow.attributes = lp

                dialogWindow.setWindowAnimations(R.style.pickerview_anim)
                dialogWindow.setGravity(mPickerOptions?.dialogGravity ?: Gravity.BOTTOM)
                mDialog!!.setOnDismissListener(DialogInterface.OnDismissListener {
                    if (onDismissListener != null) {
                        onDismissListener!!.onDismiss(this@BasePickerView)
                    }
                })
            }
        }
    }

    private fun initTextColors() {
        mPickerOptions?.apply {
            if (context != null) {
                textColorCenter = ContextCompat.getColor(context!!, R.color.black)
                textColorOut = ContextCompat.getColor(context!!, R.color.black)
            }
        }
    }

    private fun showDialog() {
        if (mDialog != null) {
            mDialog!!.show()
        }
    }

    private fun dismissDialog() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
    }

    fun getDialogContainerLayout(): ViewGroup? {
        return contentContainer
    }

    fun getDialog(): Dialog? {
        return mDialog
    }

    open fun isDialog(): Boolean {
        return false
    }

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the animGravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    private fun getAnimationResource(gravity: Int, isInAnimation: Boolean): Int {
        when (gravity) {
            Gravity.BOTTOM -> return if (isInAnimation) R.anim.slide_in_up else R.anim.slid_out_down
        }
        return ANIMATION_INVALID
    }

    companion object {
        private const val ANIMATION_INVALID = -1
    }
}