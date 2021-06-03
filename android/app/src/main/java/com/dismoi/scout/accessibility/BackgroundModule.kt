package com.dismoi.scout.accessibility

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

import com.facebook.react.bridge.Callback
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
        if (settingValue.indexOf(string = BackgroundModule.reactContext.packageName) > -1) {
          return true
        }
      }
    }
    return false
  }

  @ReactMethod
  fun isAccessibilityEnabled(callback: Callback) {
    if (!isAccessibilitySettingsOn(BackgroundModule.reactContext.applicationContext)) {
      callback.invoke("0", null)
    } else {
      callback.invoke("1", null)
      val currentActivity = currentActivity
      val startActivity = BackgroundModule.reactContext.packageManager
        .getLaunchIntentForPackage(BackgroundModule.reactContext.packageName)
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
    private lateinit var reactContext: ReactApplicationContext

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
    fun sendLeavingChromeAppEventToReactNative(params: String?) {
      sendEventToReactNative(reactContext, "EventFromLeavingChromeApp", params)
    }
  }
}
