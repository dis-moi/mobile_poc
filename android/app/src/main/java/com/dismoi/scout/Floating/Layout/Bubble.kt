package com.dismoi.scout.Floating.Layout

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import com.dismoi.scout.R

class Bubble(context: Context, attrs: AttributeSet?) : Layout(context, attrs) {
  private var initialTouchX = 0f
  private var initialTouchY = 0f
  private var initialX = 0
  private var initialY = 0
  private var onBubbleRemoveListener: OnBubbleRemoveListener? = null
  private var onBubbleClickListener: OnBubbleClickListener? = null
  private var lastTouchDown: Long = 0
  private val animator: MoveAnimator
  private var screenWidth = 0
  private var shouldStickToWall = true
  fun setOnBubbleRemoveListener(listener: OnBubbleRemoveListener?) {
    onBubbleRemoveListener = listener
  }

  fun setOnBubbleClickListener(listener: OnBubbleClickListener?) {
    onBubbleClickListener = listener
  }

  fun setShouldStickToWall(shouldStick: Boolean) {
    shouldStickToWall = shouldStick
  }

  fun notifyBubbleRemoved() {
    if (onBubbleRemoveListener != null) {
      onBubbleRemoveListener!!.onBubbleRemoved(this)
    }
  }

  private fun initializeView() {
    isClickable = true
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    playAnimation()
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event != null) {
      when (event.action) {
        MotionEvent.ACTION_DOWN -> {
          initialX = viewParams!!.x
          initialY = viewParams!!.y
          initialTouchX = event.rawX
          initialTouchY = event.rawY
          playAnimationClickDown()
          lastTouchDown = System.currentTimeMillis()
          updateSize()
          animator.stop()
        }
        MotionEvent.ACTION_MOVE -> {
          val x = initialX + (event.rawX - initialTouchX).toInt()
          val y = initialY + (event.rawY - initialTouchY).toInt()
          viewParams!!.x = x
          viewParams!!.y = y
          windowManager!!.updateViewLayout(this, viewParams)
          if (layoutCoordinator != null) {
            layoutCoordinator!!.notifyBubblePositionChanged(this, x, y)
          }
        }
        MotionEvent.ACTION_UP -> {
          goToWall()
          if (layoutCoordinator != null) {
            layoutCoordinator!!.notifyBubbleRelease(this)
            playAnimationClickUp()
          }
          if (System.currentTimeMillis() - lastTouchDown < TOUCH_TIME_THRESHOLD) {
            if (onBubbleClickListener != null) {
              onBubbleClickListener!!.onBubbleClick(this)
            }
          }
        }
      }
    }
    return super.onTouchEvent(event)
  }

  private fun playAnimation() {
    if (!isInEditMode) {
      val animator = AnimatorInflater
              .loadAnimator(context, R.animator.bubble_shown_animator) as AnimatorSet
      animator.setTarget(this)
      animator.start()
    }
  }

  private fun playAnimationClickDown() {
    if (!isInEditMode) {
      val animator = AnimatorInflater
              .loadAnimator(context, R.animator.bubble_down_click_animator) as AnimatorSet
      animator.setTarget(this)
      animator.start()
    }
  }

  private fun playAnimationClickUp() {
    if (!isInEditMode) {
      val animator = AnimatorInflater
              .loadAnimator(context, R.animator.bubble_up_click_animator) as AnimatorSet
      animator.setTarget(this)
      animator.start()
    }
  }

  private fun updateSize() {
    val metrics = DisplayMetrics()
    windowManager!!.defaultDisplay.getMetrics(metrics)
    val display = windowManager!!.defaultDisplay
    val size = Point()
    display.getSize(size)
    screenWidth = size.x - getWidth()
  }

  interface OnBubbleRemoveListener {
    fun onBubbleRemoved(bubble: Bubble?)
  }

  interface OnBubbleClickListener {
    fun onBubbleClick(bubble: Bubble?)
  }

  fun goToWall() {
    if (shouldStickToWall) {
      val middle = screenWidth / 2
      val nearestXWall = if (viewParams!!.x >= middle) screenWidth.toFloat() else 0.toFloat()
      animator.start(nearestXWall, viewParams!!.y.toFloat())
    }
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
    fun start(x: Float, y: Float) {
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

    fun stop() {
      handler.removeCallbacks(this)
    }
  }

  companion object {
    private const val TOUCH_TIME_THRESHOLD = 150
  }

  init {
    animator = MoveAnimator()
    windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    initializeView()
  }
}