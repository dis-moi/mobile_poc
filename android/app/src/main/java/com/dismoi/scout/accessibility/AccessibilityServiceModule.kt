package com.dismoi.scout.accessibility

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter

class AccessibilityServiceModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return "AccessibilityServiceModule"
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
        if (settingValue.indexOf(string = reactContext!!.packageName) > -1) {
          return true
        }
      }
    }
    return false
  }

  @ReactMethod
  fun isAccessibilityEnabled(callback: Callback) {
    if (!isAccessibilitySettingsOn(reactContext!!.applicationContext)) {
      callback.invoke("0", null)
    } else {
      callback.invoke("1", null)
      val currentActivity = currentActivity
      val startActivity = reactContext!!.packageManager
        .getLaunchIntentForPackage(reactContext!!.packageName)
      startActivity!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      currentActivity!!.startActivity(startActivity)
    }
  }

  @ReactMethod
  fun redirectToAppAccessibilitySettings() {
    val currentActivity = currentActivity
    currentActivity!!.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
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
        .getJSModule<RCTDeviceEventEmitter>(RCTDeviceEventEmitter::class.java)
        .emit(eventName, params)
    }

    @JvmStatic
    fun sendChromeUrlEventToReactNative(params: String?) {
      sendEventToReactNative(reactContext, "EventFromChromeURL", params)
    }

    @JvmStatic
    fun sendEventFromAccessibilityServicePermission(params: String?) {
      sendEventToReactNative(reactContext, "EventFromAccessibilityServicePermission", params)
    }

    @JvmStatic
    fun sendLeavingChromeAppEventToReactNative(params: String?) {
      sendEventToReactNative(reactContext, "EventFromLeavingChromeApp", params)
    }
  }

  init {
    Companion.reactContext = reactContext
  }
}
