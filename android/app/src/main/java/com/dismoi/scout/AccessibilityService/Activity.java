package com.dismoi.scout.AccessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class Activity extends AccessibilityService {

  @Override
  protected void onServiceConnected() {
    AccessibilityServiceInfo info = getServiceInfo();
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
    info.flags = AccessibilityServiceInfo.DEFAULT;
    info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
    info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
    info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
    info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
    info.notificationTimeout = 100;
    info.packageNames = null;

    this.setServiceInfo(info);
    AccessibilityServiceModule.prepareEventFromAccessibilityServicePermission("true");
  }

  @Override
  public void onDestroy() {
    AccessibilityServiceModule.prepareEventFromAccessibilityServicePermission("false");
  }
  
  private String captureUrl(AccessibilityNodeInfo info) {
    List<AccessibilityNodeInfo> nodes = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      nodes = info.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar");
    }
    if (nodes == null || nodes.size() <= 0) {
      return null;
    }
    AccessibilityNodeInfo addressBarNodeInfo = nodes.get(0);
    
    String url = null;
    if (addressBarNodeInfo.getText() == null) {
      return null;
    }
    url = addressBarNodeInfo.getText().toString();
    addressBarNodeInfo.recycle();
    return url;
  }

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    AccessibilityNodeInfo parentNodeInfo = event.getSource();
    if (parentNodeInfo == null) {
      return;
    }

    String usedPackage = event.getPackageName().toString();

    if (usedPackage.contains("launcher")) {
      AccessibilityServiceModule.prepareEventFromLeavingChromeApp("true");
    }

    String capturedUrl = captureUrl(parentNodeInfo);
    if (capturedUrl == null) {
      return;
    }

    AccessibilityServiceModule.prepareEventFromLeavingChromeApp("false");
    AccessibilityServiceModule.prepareEventFromChromeURL(capturedUrl);
  }

  @Override
  public void onInterrupt() {
    AccessibilityServiceModule.prepareEventFromLeavingChromeApp("true");
  }
}