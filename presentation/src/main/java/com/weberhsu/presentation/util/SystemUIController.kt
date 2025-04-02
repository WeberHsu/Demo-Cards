package com.weberhsu.presentation.util

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt

object SystemUIController {

    /**
     * This function is used to get color's Luminance
     */
    private fun Int.getLuminanceCompat(): Float =
        Color.luminance(this)

    /**
     * Call this function to set whether the window's child view can appear under the status bar
     * @param isLayoutInStatusBar true: Window's child view will appear under the window's status bar false for not
     */
    fun shouldLayoutInStatusBar(window: Window, isLayoutInStatusBar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
        }

        window.decorView.systemUiVisibility = if (isLayoutInStatusBar) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        }
    }

    /**
     * This function is used to set status bar's background
     *
     * @param statusBarBackgroundColor statusBarBackgroundColor
     * @param isLight whether statusBarBackgroundColor is light or dark, if the statusBarBackgroundColor is light color, status bar's text and icon will be black
     *                otherwise white. If you let isLight null, this function will judge statusBarBackgroundColor's Luminance all by itself
     *
     * @see SystemUIController.setLightStatusBar
     */
    fun compatStatusBar(
        window: Window,
        @ColorInt statusBarBackgroundColor: Int,
        isLight: Boolean? = null
    ) {
        window.statusBarColor = statusBarBackgroundColor

        val isRealLight = isLight ?: (statusBarBackgroundColor.getLuminanceCompat() >= 0.5f)
        setLightStatusBar(window, isRealLight)
    }

    /**
     * call this function will expand window's view to cut off area.
     */
    fun layoutToCutOffArea(window: Window, shouldExpendToCutOffArea: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode = if (shouldExpendToCutOffArea)
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                else
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            }
        }
    }

    /**
     * Mark status bar's background is light which means window status bar's text and icon is black
     * @param isLight Mark status bar's background is light which means status bar's icon and text is black, false for otherwise
     */
    fun setLightStatusBar(window: Window, isLight: Boolean) {
        window.decorView.systemUiVisibility = if (isLight) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    /**
     * Mark navigation bar's background is light which means window navigation bar's text and icon is black
     * @param isLight Mark navigation bar's background is light which means navigation bar's icon and text is black, false for otherwise
     */
    fun setLightNavigationBar(window: Window, isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = if (isLight) {
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }
    }

    /**
     * This function is used to set navigation bar's background
     *
     * @param navigationBarBackgroundColor navigationBarBackgroundColor
     * @param isLight whether navigationBarBackgroundColor is light or dark, if the navigationBarBackgroundColor is light color, status bar's text and icon will be black
     *                otherwise white. If you let isLight null, this function will judge navigationBarBackgroundColor's Luminance all by itself
     *
     * @see SystemUIController.setLightStatusBar
     */
    fun compatNavigationBar(
        window: Window,
        @ColorInt navigationBarBackgroundColor: Int,
        isLight: Boolean? = null
    ) {
        window.navigationBarColor = navigationBarBackgroundColor

        val isRealLight = isLight ?: (navigationBarBackgroundColor.getLuminanceCompat() >= 0.5f)
        setLightNavigationBar(window, isRealLight)
    }


    /**
     * show or hide Status bar
     * @param shouldHide true for show, false for hide
     * @param isSticky true if use slide down let the status bar show, the bar will hide after a while
     *      *          false for otherwise
     *
     * @param isStable true if view in window set the flag fitSystemWindow this view won't jump
     *                 false for otherwise
     */
    fun hideStatusBar(
        window: Window,
        shouldHide: Boolean,
        isSticky: Boolean = true,
        isStable: Boolean = true
    ) {
        var visibility = window.decorView.systemUiVisibility

        visibility = if (shouldHide) {
            visibility or View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            visibility and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        }

        visibility = visibility.setViewFlagSticky(isSticky)
        visibility = visibility.setViewFlagStable(isStable)

        window.decorView.systemUiVisibility = visibility
    }

    /**
     * show or hide navigation bar
     *
     * @see hideStatusBar
     */
    fun hideNavigationBar(
        window: Window,
        shouldHide: Boolean,
        isSticky: Boolean = true,
        isStable: Boolean = true
    ) {
        var visibility = window.decorView.systemUiVisibility

        visibility = if (shouldHide) {
            visibility or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        } else {
            visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
        }

        visibility = visibility.setViewFlagSticky(isSticky)
        visibility = visibility.setViewFlagStable(isStable)

        window.decorView.systemUiVisibility = visibility
    }

    private fun Int.setViewFlagSticky(isSticky: Boolean): Int {
        return if (isSticky) {
            this or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            this and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        }
    }

    private fun Int.setViewFlagStable(isStable: Boolean): Int {
        return if (isStable) {
            this or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            this and View.SYSTEM_UI_FLAG_LAYOUT_STABLE.inv()
        }

    }
}