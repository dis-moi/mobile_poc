package com.dismoi.scout.AccessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class Activity extends AccessibilityService {

  @Override
  protected void onServiceConnected() {
    AccessibilityServiceInfo info = getServiceInfo();
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

    // Have to put TYPES_ALL_MASK so that I can detect events when url is changing
    info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
    info.flags = AccessibilityServiceInfo.DEFAULT;
    info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
    info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
    info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
    info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
    info.notificationTimeout = 100;
    info.packageNames = null;

    this.setServiceInfo(info);
    AccessibilityServiceModule.sendEventFromAccessibilityServicePermission("true");
  }

  @Override
  public void onDestroy() {
    AccessibilityServiceModule.sendEventFromAccessibilityServicePermission("false");
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

  private CharSequence getAppNameInScreen(String usedPackage) {
    PackageManager packageManager = this.getPackageManager();

    ApplicationInfo applicationInfo = null;
    try {
      applicationInfo = packageManager.getApplicationInfo(usedPackage, 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    return packageManager.getApplicationLabel(applicationInfo);
  }

  // private void sendEventsToReactNative(String usedPackage) {
  //   PackageManager packageManager = this.getPackageManager();

  //   ApplicationInfo applicationInfo = null;
  //   try {
  //     applicationInfo = packageManager.getApplicationInfo(usedPackage, 0);
  //   } catch (PackageManager.NameNotFoundException e) {
  //     e.printStackTrace();
  //   }

  //   return packageManager.getApplicationLabel(applicationInfo);
  // }

  private Boolean disMoiAppIsOnFocusScreen(CharSequence applicationLabelName) {
    return applicationLabelName.toString().equals("DisMoi");
  }

  private Boolean chromeAppIsOnFocusScreen(CharSequence applicationLabelName) {
    return applicationLabelName.toString().equals("Chrome");
  }

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    AccessibilityNodeInfo parentNodeInfo = event.getSource();
    if (parentNodeInfo == null) {
      return;
    }

    String packageAppNameInScreen = event.getPackageName().toString();

    CharSequence applicationLabelName = getAppNameInScreen(packageAppNameInScreen);

    if (disMoiAppIsOnFocusScreen(applicationLabelName) == true) {
      return;
    }

    if (chromeAppIsOnFocusScreen(applicationLabelName) == true) {
      String capturedUrl = captureUrl(parentNodeInfo);
      if (capturedUrl == null) {
        return;
      }
  
      AccessibilityServiceModule.sendLeavingChromeAppEventToReactNative("false");
      AccessibilityServiceModule.sendChromeUrlEventToReactNative(capturedUrl);
    }
    
    if (chromeAppIsOnFocusScreen(applicationLabelName) == false) {
      AccessibilityServiceModule.sendLeavingChromeAppEventToReactNative("true");
    }
  }

  @Override
  public void onInterrupt() {
    AccessibilityServiceModule.sendLeavingChromeAppEventToReactNative("true");
  }
}