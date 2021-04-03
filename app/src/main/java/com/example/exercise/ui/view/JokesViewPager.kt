package com.example.exercise.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class JokesViewPager(context: Context, set:AttributeSet) : ViewPager(context,set) {
    private val activityContext:Context  = context
    private var gestureDetector: GestureDetector
    
     var gestureEventListener :GestureEvents? = null
        set(value) { field = value }

    init { 
        gestureDetector = GestureDetector(activityContext, GestureListener())
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.onTouchEvent(ev)
    }

    
    companion object {
        private val SWIPE_THRESHOLD = 20
        private val SWIPE_VELOCITY_THRESHOLD = 50
    }
 
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (e1.x > e2.x) {
                            gestureEventListener?.onSwipeRight()
                        } 
                        result = true
                    }
                } 
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

   interface GestureEvents {
        fun onSwipeRight() 
   }
    
   }
