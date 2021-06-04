package com.dismoi.scout.accessibility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.canDrawOverlays
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import javax.annotation.Nonnull

class BackgroundModule(@Nonnull reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  private var reactContext: ReactApplicationContext = reactContext

  @Nonnull
  override fun getName(): String {
    return "Background"
  }

  @ReactMethod
  fun startService() {
    reactContext.startService(Intent(reactContext, BackgroundService::class.java))
    val launchIntent = reactContext.packageManager.getLaunchIntentForPackage(
      reactContext.packageName
    )
    if (launchIntent != null) {
      reactContext.startActivity(launchIntent)
    }
  }

  @ReactMethod
  fun stopService() {
    reactContext.stopService(Intent(reactContext, BackgroundService::class.java))
  }

  private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
    var accessibilityEnabled = Settings.Secure.getInt(
      mContext.applicationContext.contentResolver,
      Settings.Secure.ACCESSIBILITY_ENABLED
    )
    if (accessibilityEnabled == 1) {
      val settingValue = Settings.Secure.getString(
        mContext.applicationContext.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
      )
      if (settingValue != null) {
        if (settingValue.indexOf(string = reactContext.packageName) > -1) {
          return true
        }
      }
    }
    return false
  }

  private fun hasPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Settings.canDrawOverlays(reactContext)
    } else true
  }

  @ReactMethod
  fun isAccessibilityEnabled(callback: Callback) {
    if (!isAccessibilitySettingsOn(reactContext)) {
      callback.invoke("0", null)
    } else {
      callback.invoke("1", null)
    }
  }

  @ReactMethod
  fun canDrawOverlay(callback: Callback) {
    if (hasPermission()) {
      callback.invoke("1", null)

    } else {
      callback.invoke("0", null)
    }
  }

  private fun requestPermissionAction() {
    var intent: Intent? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse(
          "package:" + reactContext.packageName
        )
      )
    }
    val bundle = Bundle()
    reactContext.startActivityForResult(intent, 0, bundle)
  }

  @ReactMethod
  fun redirectToAppAccessibilitySettings(promise: Promise) {
    val currentActivity = currentActivity
    currentActivity!!.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    promise.resolve("")
  }

  @ReactMethod
  fun redirectToAppAdvancedSettings(promise: Promise) {
    requestPermissionAction()
    // val currentActivity = currentActivity
    // startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, data: "package:" + "com.dismoi.scout"))
    promise.resolve("")
  }

  companion object {
    private var reactContext: ReactApplicationContext? = null

    private fun sendEventToReactNative(
      reactContext: ReactContext?,
      eventName: String,
      params: String?
    ) {
      if (reactContext == null) {
        return
      }
      reactContext
        .getJSModule<DeviceEventManagerModule.RCTDeviceEventEmitter>(
          DeviceEventManagerModule.RCTDeviceEventEmitter::class.java
        ).emit(eventName, params)
    }

    @JvmStatic
    fun sendEventFromAccessibilityServicePermission(params: String?) {
      sendEventToReactNative(reactContext, "EventFromAccessibilityServicePermission", params)
    }

    @JvmStatic
    fun sendEventFromOverlayPermission(params: String?) {
      sendEventToReactNative(reactContext, "EventFromOverlayPermission", params)
    }
  }

  init {
    Companion.reactContext = reactContext
  }
}
