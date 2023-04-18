package com.facultate.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class DebouncingRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var lastClickTime: Long = 0
    private val clickDelay:Long = 500 // Set the desired delay here

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e?.action == MotionEvent.ACTION_UP) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > clickDelay) {
                lastClickTime = currentTime
                return super.onTouchEvent(e)
            }
        }
        return false
    }
}
