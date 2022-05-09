package com.kollektors.fields_verification.ui.utils.android

import android.content.res.Resources
import android.view.View


private class PreventMultiClickListener(
    private val delay: Long,
    private val listener: View.OnClickListener,
) : View.OnClickListener {

    companion object {
        const val DEFAULT_DELAY = 700L
    }

    private var clickTime = 0L

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - clickTime > delay) {
            listener.onClick(v)
        }
        clickTime = currentTime
    }
}

internal fun View.onDelayClick(
    delay: Long = PreventMultiClickListener.DEFAULT_DELAY,
    listener: View.OnClickListener
) {
    setOnClickListener(PreventMultiClickListener(delay, listener))
}

internal inline fun View.onDelayClick(
    delay: Long = PreventMultiClickListener.DEFAULT_DELAY,
    crossinline listener: (View?) -> Unit
) {
    onDelayClick(delay, View.OnClickListener { listener(it) })
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
