package com.dismoi.scout.floating.layout

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.WindowManager
import com.dismoi.scout.R

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class Message(context: Context, attrs: AttributeSet?) : Layout(context, attrs) {

  lateinit var star: ImageView
  lateinit var rotateButton: Button
  lateinit var translateButton: Button
  lateinit var scaleButton: Button
  lateinit var fadeButton: Button
  lateinit var colorizeButton: Button
  lateinit var showerButton: Button

  override fun onAttachedToWindow() {
    Log.d("Notification", "Hello")
    super.onAttachedToWindow()
    // playAnimation()
  }

  private fun playAnimation() {
    //val animator = ObjectAnimator.ofFloat(this, View.ROTATION, -360f, 0f)
    //animator.duration = 1000
    //animator.start()

    // val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 200f)
    // animator.repeatCount = 1
    // animator.repeatMode = ObjectAnimator.REVERSE
    // animator.start()

    // newStar, View.TRANSLATION_Y, -starH, containerH + starH

    // val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 300f, 500f)

    // animator.duration = 1000

    // // animator.repeatMode = ObjectAnimator.REVERSE
    // animator.start()


    // val animator = AnimatorInflater
    //     .loadAnimator(context, R.animator.message_shown_animator) as AnimatorSet
    // animator.setTarget(this)
    // animator.start()
  }

  //private fun move(deltaX: Float, deltaY: Float) {
    // viewParams!!.x += deltaX.toInt()
    // viewParams!!.y += deltaY.toInt()
    // windowManager!!.updateViewLayout(this, viewParams)
  // }

  // private inner class MoveAnimator : Runnable {
  //   private val handler = Handler(Looper.getMainLooper())
  //   private var destinationX = 0f
  //   private var destinationY = 0f
  //   private var startingTime: Long = 0
  //   private fun start(x: Float, y: Float) {
  //     destinationX = x
  //     destinationY = y
  //     startingTime = System.currentTimeMillis()
  //     handler.post(this)
  //   }

  //   override fun run() {
  //     if (rootView != null && rootView.parent != null) {
  //       val progress = Math.min(1f, (System.currentTimeMillis() - startingTime) / 400f)
  //       val deltaX = (destinationX - viewParams!!.x) * progress
  //       val deltaY = (destinationY - viewParams!!.y) * progress
  //       move(deltaX, deltaY)
  //       if (progress < 1) {
  //         handler.post(this)
  //       }
  //     }
  //   }

  //   private fun stop() {
  //     handler.removeCallbacks(this)
  //   }
  // }

  init {
    // animator = MoveAnimator()
    // c = context
    // windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  }
}
