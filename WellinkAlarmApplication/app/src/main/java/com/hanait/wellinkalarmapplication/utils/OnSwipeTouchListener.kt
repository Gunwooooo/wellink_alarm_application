package com.hanait.wellinkalarmapplication.utils

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

abstract class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 5
        private const val SWIPE_VELOCITY_THRESHOLD = 5
        private val eventList = ArrayList<Pair<Float, Float>>()
    }
    private val gestureDetector: GestureDetector
    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if (event != null) {
            eventList.add(Pair(event.y, event.x))
        }
        return gestureDetector.onTouchEvent(event)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val event1 = eventList[0]
            val event2 = eventList.last()
            val distanceX = event2.second - event1.second
            val distanceY = event2.first - event1.first
            eventList.clear()
            if (
                abs(distanceX) > abs(distanceY) &&
                abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return true
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}