package com.ubc.words.common

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, val rv: RecyclerView, val listener: OnItemClickListener )
    : RecyclerView.OnItemTouchListener {

    val gestureDetector = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            val child  = rv.findChildViewUnder(e!!.getX(), e.getY())
            if (child != null) {
                listener.onLongItemClick(child, rv.getChildAdapterPosition(child))
            }
        }
    })

    interface OnItemClickListener {
        fun onLongItemClick(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.getX(), e.getY())
        if (child != null && gestureDetector.onTouchEvent(e)) {
            listener.onLongItemClick(child, rv.getChildAdapterPosition(child))
            return true
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }
}