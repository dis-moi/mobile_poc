package com.dismoi.scout.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendChromeUrlEventToReactNative
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendEventFromAccessibilityServicePermission
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendLeavingChromeAppEventToReactNative

class Activity : AccessibilityService() {

  private val previousUrlDetections: HashMap<String, Long> = HashMap()

  override fun onServiceConnected() {
    val info = serviceInfo
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
    info.packageNames = packageNames()
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
    // throttling of accessibility event notification
    info.notificationTimeout = 300
    // support ids interception
    info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
      AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
    this.serviceInfo = info

    sendEventFromAccessibilityServicePermission("true")
  }

  override fun onDestroy() {
    sendEventFromAccessibilityServicePermission("false")
  }

  override fun onInterrupt() {
    sendLeavingChromeAppEventToReactNative("true")
  }

  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig): String? {
    val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
    if (nodes == null || nodes.size <= 0) {
      return null
    }
    val addressBarNodeInfo = nodes[0]
    var url: String? = null
    if (addressBarNodeInfo.text != null) {
      url = addressBarNodeInfo.text.toString()
    }
    addressBarNodeInfo.recycle()
    return url
  }

  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  override fun onAccessibilityEvent(event: AccessibilityEvent) {
    val parentNodeInfo = event.source ?: return
    val packageName = event.packageName.toString()
    var browserConfig: SupportedBrowserConfig? = null
    for (supportedConfig in getSupportedBrowsers()) {
      if (supportedConfig.packageName == packageName) {
        browserConfig = supportedConfig
      }
    }

    // this is not supported browser, so exit
    if (browserConfig == null) {
      return
    }
    val capturedUrl = captureUrl(parentNodeInfo, browserConfig)
    parentNodeInfo.recycle()

    // we can't find a url. Browser either was updated or opened page without url text field
    if (capturedUrl == null) {
      return
    }
    val eventTime = event.eventTime
    val detectionId = "$packageName, and url $capturedUrl"
    val lastRecordedTime =
      if (previousUrlDetections.containsKey(detectionId)) {
        previousUrlDetections[detectionId]!!
      } else 0.toLong()
    // some kind of redirect throttling
    if (eventTime - lastRecordedTime > 2000) {
      previousUrlDetections[detectionId] = eventTime

      val className = event.className

      if (className == "android.widget.EditText") {
        sendChromeUrlEventToReactNative(capturedUrl)
      } else {
        sendChromeUrlEventToReactNative("")
      }
    }
  }

  private fun packageNames(): Array<String> {
    val packageNames: MutableList<String> = ArrayList()
    for (config in getSupportedBrowsers()) {
      packageNames.add(config.packageName)
    }
    return packageNames.toTypedArray()
  }

  private class SupportedBrowserConfig(var packageName: String, var addressBarId: String)

  /** @return a list of supported browser configs
   * This list could be instead obtained from remote server to support future browser updates without updating an app
   */
  private fun getSupportedBrowsers(): List<SupportedBrowserConfig> {
    val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
    browsers.add(SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"))
    return browsers
  }
}
