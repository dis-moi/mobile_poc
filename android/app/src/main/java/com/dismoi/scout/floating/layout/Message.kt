package com.dismoi.scout.floating.layout

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.WindowManager

class Message(context: Context, attrs: AttributeSet?) : Layout(context, attrs) {
  private val animator: MoveAnimator
  private val c: Context

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
  }

  private fun move(deltaX: Float, deltaY: Float) {
    viewParams!!.x += deltaX.toInt()
    viewParams!!.y += deltaY.toInt()
    windowManager!!.updateViewLayout(this, viewParams)
  }

  private inner class MoveAnimator : Runnable {
    private val handler = Handler(Looper.getMainLooper())
    private var destinationX = 0f
    private var destinationY = 0f
    private var startingTime: Long = 0
    private fun start(x: Float, y: Float) {
      destinationX = x
      destinationY = y
      startingTime = System.currentTimeMillis()
      handler.post(this)
    }

    override fun run() {
      if (rootView != null && rootView.parent != null) {
        val progress = Math.min(1f, (System.currentTimeMillis() - startingTime) / 400f)
        val deltaX = (destinationX - viewParams!!.x) * progress
        val deltaY = (destinationY - viewParams!!.y) * progress
        move(deltaX, deltaY)
        if (progress < 1) {
          handler.post(this)
        }
      }
    }

    private fun stop() {
      handler.removeCallbacks(this)
    }
  }

  init {
    animator = MoveAnimator()
    c = context
    windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  }
}
