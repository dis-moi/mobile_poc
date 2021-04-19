package com.dismoi.scout.floating.layout

import android.view.View
import android.view.WindowManager
import com.dismoi.scout.floating.FloatingService

class Coordinator private constructor() {
  private var _trashView: Trash? = null
  private var windowManager: WindowManager? = null
  private var bubblesService: FloatingService? = null

  fun notifyBubblePositionChanged(bubble: Bubble, x: Int, y: Int) {
    if (_trashView != null) {
      _trashView!!.visibility = View.VISIBLE
      if (checkIfBubbleIsOverTrash(bubble)) {
        _trashView!!.applyMagnetism()
        _trashView!!.vibrate()
        applyTrashMagnetismToBubble(bubble)
      } else {
        _trashView!!.releaseMagnetism()
      }
    }
  }

  private fun applyTrashMagnetismToBubble(bubble: Bubble) {
    val trashContentView = trashContent
    val trashCenterX = trashContentView.left + trashContentView.measuredWidth / 2
    val trashCenterY = trashContentView.top + trashContentView.measuredHeight / 2
    val x = trashCenterX - bubble.measuredWidth / 2
    val y = trashCenterY - bubble.measuredHeight / 2
    bubble.viewParams!!.x = x
    bubble.viewParams!!.y = y
    windowManager!!.updateViewLayout(bubble, bubble.viewParams)
  }

  private fun checkIfBubbleIsOverTrash(bubble: Bubble): Boolean {
    var result = false
    if (_trashView!!.visibility == View.VISIBLE) {
      val trashContentView = trashContent
      val trashWidth = trashContentView.measuredWidth
      val trashHeight = trashContentView.measuredHeight
      val trashLeft = trashContentView.left - trashWidth / 2
      val trashRight = trashContentView.left + trashWidth + trashWidth / 2
      val trashTop = trashContentView.top - trashHeight / 2
      val trashBottom = trashContentView.top + trashHeight + trashHeight / 2
      val bubbleWidth = bubble.measuredWidth
      val bubbleHeight = bubble.measuredHeight
      val bubbleLeft = bubble.viewParams!!.x
      val bubbleRight = bubbleLeft + bubbleWidth
      val bubbleTop = bubble.viewParams!!.y
      val bubbleBottom = bubbleTop + bubbleHeight
      if (bubbleLeft >= trashLeft && bubbleRight <= trashRight) {
        if (bubbleTop >= trashTop && bubbleBottom <= trashBottom) {
          result = true
        }
      }
    }
    return result
  }

  fun notifyBubbleRelease(bubble: Bubble) {
    if (_trashView != null) {
      if (checkIfBubbleIsOverTrash(bubble)) {
        bubblesService!!.removeBubble(bubble)
      }
      _trashView!!.visibility = View.GONE
    }
  }

  class Builder(service: FloatingService?) {
    private val layoutCoordinator: Coordinator?
    fun setTrashView(trashView: Trash?): Builder {
      layoutCoordinator!!._trashView = trashView
      return this
    }

    fun setWindowManager(windowManager: WindowManager?): Builder {
      layoutCoordinator!!.windowManager = windowManager
      return this
    }

    fun build(): Coordinator? {
      return layoutCoordinator
    }

    init {
      layoutCoordinator = instance
      layoutCoordinator!!.bubblesService = service
    }
  }

  private val trashContent: View
    private get() = _trashView!!.getChildAt(0)

  companion object {
    private var INSTANCE: Coordinator? = null
    private val instance: Coordinator?
      private get() {
        if (INSTANCE == null) {
          INSTANCE = Coordinator()
        }
        return INSTANCE
      }
  }
}
