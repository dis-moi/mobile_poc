package com.dismoi.scout.Floating.Layout

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.WindowManager
import com.dismoi.scout.R

class Message(context: Context, attrs: AttributeSet?) : Layout(context, attrs) {
  private val animator: MoveAnimator
  private val c: Context

  private fun initializeView() {
    Log.v("Notifications", "initialize view")
    isClickable = true
  }

  override fun onAttachedToWindow() {
    Log.v("Notifications", "on attached to window")
    super.onAttachedToWindow()
    playAnimation()
  }

  private fun playAnimation() {
    Log.v("Notifications", "play animation")
    val animator = AnimatorInflater
            .loadAnimator(context, R.animator.message_slide_up) as AnimatorSet
    animator.setTarget(this)
    animator.start()
  }

  private fun move(deltaX: Float, deltaY: Float) {
    Log.v("Notifications", "move")
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
      Log.v("Notifications", "start")
      destinationX = x
      destinationY = y
      startingTime = System.currentTimeMillis()
      handler.post(this)
    }

    override fun run() {
      Log.v("Notifications", "run")
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
      Log.v("Notifications", "stop")
      handler.removeCallbacks(this)
    }
  }

  init {
    Log.v("Notifications", "Message view animator")
    animator = MoveAnimator()
    c = context
    windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    initializeView()
  }
}