package com.dismoi.scout.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ApplicationInfo
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendChromeUrlEventToReactNative
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendEventFromAccessibilityServicePermission
import com.dismoi.scout.accessibility.AccessibilityServiceModule.Companion.sendLeavingChromeAppEventToReactNative

class Activity : AccessibilityService() {
  override fun onServiceConnected() {
    val info = serviceInfo
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED

    // Have to put TYPES_ALL_MASK so that I can detect events when url is changing
    info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
    info.flags = AccessibilityServiceInfo.DEFAULT
    info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
    }
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
    }
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    info.notificationTimeout = 100
    info.packageNames = null
    this.serviceInfo = info
    sendEventFromAccessibilityServicePermission("true")
  }

  override fun onDestroy() {
    sendEventFromAccessibilityServicePermission("false")
  }

  private fun captureUrl(info: AccessibilityNodeInfo): String? {
    var nodes: List<AccessibilityNodeInfo>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      nodes = info.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar")
    }
    if (nodes == null || nodes.size <= 0) {
      return null
    }
    val addressBarNodeInfo = nodes[0]
    if (addressBarNodeInfo.text == null) {
      return null
    }
    var url: String = addressBarNodeInfo.text.toString()
    addressBarNodeInfo.recycle()
    return url
  }

  private fun getAppNameInScreen(usedPackage: String): CharSequence {
    val packageManager = this.packageManager
    var applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(usedPackage, 0)
    return packageManager.getApplicationLabel(applicationInfo)
  }

  private fun disMoiAppIsOnFocusScreen(applicationLabelName: CharSequence): Boolean {
    return applicationLabelName.toString() == "DisMoi"
  }

  private fun chromeAppIsOnFocusScreen(applicationLabelName: CharSequence): Boolean {
    return applicationLabelName.toString() == "Chrome"
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent) {
    val parentNodeInfo = event.source ?: return
    val packageAppNameInScreen = event.packageName.toString()
    val type = event.eventType
    val applicationLabelName = getAppNameInScreen(packageAppNameInScreen)
    if (type == 32 || type == 16) {
      if (disMoiAppIsOnFocusScreen(applicationLabelName) == true) {
        return
      }
      if (chromeAppIsOnFocusScreen(applicationLabelName) == false) {
        sendLeavingChromeAppEventToReactNative("true")
      }
      if (chromeAppIsOnFocusScreen(applicationLabelName) == true) {
        val capturedUrl = captureUrl(parentNodeInfo) ?: return
        sendLeavingChromeAppEventToReactNative("false")
        sendChromeUrlEventToReactNative(capturedUrl)
      }
    }
  }

  override fun onInterrupt() {
    sendLeavingChromeAppEventToReactNative("true")
  }
}
