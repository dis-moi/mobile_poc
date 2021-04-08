package com.dismoi.scout.Floating

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.dismoi.scout.Floating.Layout.Bubble
import com.dismoi.scout.Floating.Layout.Bubble.OnBubbleClickListener
import com.dismoi.scout.Floating.Layout.Bubble.OnBubbleRemoveListener
import com.dismoi.scout.Floating.Layout.Message
import com.dismoi.scout.Floating.Manager
import com.dismoi.scout.R
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import android.util.Log
import android.widget.LinearLayout

class FloatingModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private var bubblesManager: Manager? = null
  private var messagesManager: Manager? = null
  private var bubbleDisMoiView: Bubble? = null
  private var messageDisMoiView: Message? = null

  @ReactMethod
  fun reopenApp() {
    val launchIntent = reactContext.packageManager.getLaunchIntentForPackage(reactContext.packageName)
    if (launchIntent != null) {
      reactContext.startActivity(launchIntent)
    }
  }

  override fun getName(): String {
    return "FloatingModule"
  }

  @ReactMethod
  fun showFloatingDisMoiBubble(x: Int, y: Int, promise: Promise) {
    try {
      addNewFloatingDisMoiBubble(x, y)
      promise.resolve("")
    } catch (e: Exception) {
      promise.reject("")
    }
  }

  @ReactMethod
  fun showFloatingDisMoiMessage(x: Int, y: Int, promise: Promise) {
    if (messageDisMoiView == null) {
      try {
        addNewFloatingDisMoiMessage(x, y)
        promise.resolve("")
      } catch (e: Exception) {
        promise.reject("")
      }
    }
  }

  @ReactMethod
  fun hideFloatingDisMoiBubble(promise: Promise) {
    try {
      removeDisMoiBubble()
      promise.resolve("")
    } catch (e: Exception) {
      promise.reject("")
    }
  }

  @ReactMethod
  fun hideFloatingDisMoiMessage(promise: Promise) {
    try {
      removeDisMoiMessage()
      messageDisMoiView = null
      promise.resolve("")
    } catch (e: Exception) {
      promise.reject("")
    }
  }

  @ReactMethod
  fun requestPermission(promise: Promise) {
    requestPermissionAction(promise)
  }

  @ReactMethod
  fun checkPermission(promise: Promise) {
    try {
      promise.resolve(hasPermission())
    } catch (e: Exception) {
      promise.reject("")
    }
  }

  @ReactMethod
  fun initialize(promise: Promise) {
    try {
      initializeBubblesManager()
      initializeDisMoiMessageManager()
      promise.resolve("")
    } catch (e: Exception) {
      promise.reject("")
    }
  }

  private fun addNewFloatingDisMoiMessageDetail(x: Int, y: Int) {
    removeDisMoiBubble()
    removeDisMoiMessage()
    messageDisMoiView = LayoutInflater.from(reactContext).inflate(R.layout.message, messageDisMoiView, false) as Message
    val imageButton = messageDisMoiView!!.findViewById<View>(R.id.close) as ImageButton
    imageButton.setOnClickListener { sendEventToReactNative("floating-dismoi-message-press") }

    messagesManager!!.addDisMoiMessage(messageDisMoiView!!, x, y)
  }

  private fun addNewFloatingDisMoiMessage(x: Int, y: Int) {
    removeDisMoiBubble()
    messageDisMoiView = LayoutInflater.from(reactContext).inflate(R.layout.messages, messageDisMoiView, false) as Message
    val imageButton = messageDisMoiView!!.findViewById<View>(R.id.close) as ImageButton
    imageButton.setOnClickListener { sendEventToReactNative("floating-dismoi-message-press") }

    val layoutMessage = messageDisMoiView!!.findViewById<View>(R.id.message) as LinearLayout
    layoutMessage.setOnClickListener { 
      Log.d("Notification", "button press") 
      addNewFloatingDisMoiMessageDetail(10, 1500)
    }
    messagesManager!!.addDisMoiMessage(messageDisMoiView!!, x, y)
  }

  private fun addNewFloatingDisMoiBubble(x: Int, y: Int) {

    Log.d("Notification", "add new dismoi bubble")

    bubbleDisMoiView = LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, bubbleDisMoiView, false) as Bubble
    
    Log.d("Notification", "after layout")
    
    bubbleDisMoiView!!.setOnBubbleRemoveListener(object : OnBubbleRemoveListener {
      override fun onBubbleRemoved(bubble: Bubble?) {
        bubbleDisMoiView = null
        sendEventToReactNative("floating-dismoi-bubble-remove")
      }
    })
    bubbleDisMoiView!!.setOnBubbleClickListener(object : OnBubbleClickListener {
      override fun onBubbleClick(bubble: Bubble?) {
        sendEventToReactNative("floating-dismoi-bubble-press")
      }
    })
    bubbleDisMoiView!!.setShouldStickToWall(true)

    Log.d("Notification", "add dis moi bubble")

    bubblesManager!!.addDisMoiBubble(bubbleDisMoiView, x, y)
  }

  private fun hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Settings.canDrawOverlays(reactContext)
    } else true
  }

  private fun removeDisMoiBubble() {
    if (bubbleDisMoiView != null) {
      bubblesManager!!.removeBubble(bubbleDisMoiView)
    }
  }

  private fun removeDisMoiMessage() {
    if (messageDisMoiView != null) {
      messagesManager!!.removeDisMoiMessage(messageDisMoiView)
    }
  }

  fun requestPermissionAction(promise: Promise) {
    if (!hasPermission()) {
      var intent: Intent? = null
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactContext.packageName))
      }
      val bundle = Bundle()
      reactContext.startActivityForResult(intent, 0, bundle)
    }
    if (hasPermission()) {
      promise.resolve("")
    } else {
      promise.reject("")
    }
  }

  private fun initializeDisMoiMessageManager() {
    Log.d("Notification", "initialize dismoi message")
    messagesManager = Manager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(object : OnCallback {
      override fun onInitialized() {}
    }).build()
    messagesManager!!.initialize()
  }

  private fun initializeBubblesManager() {
    Log.d("Notification", "initialize dismoi bubble")
    bubblesManager = Manager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(object : OnCallback {
      override fun onInitialized() {}
    }).build()
    bubblesManager!!.initialize()
  }

  private fun sendEventToReactNative(eventName: String) {
    val params = Arguments.createMap()
    reactContext
            .getJSModule(RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
  }
}