package com.weberhsu.presentation.extensions

import android.view.View
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.SoftwareKeyboardControllerCompat

/**
 * author : weber
 * desc :
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun EditText.showSoftKeyboard() {
    requestFocus()
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    postDelayed({
        manager.showSoftInput(this, 0)
    }, 300)
}

fun View.hideSoftKeyboard() {
    SoftwareKeyboardControllerCompat(this).hide()
}