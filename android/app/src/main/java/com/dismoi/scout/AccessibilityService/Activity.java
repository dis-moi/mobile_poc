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

    String packageName = event.getPackageName().toString();
    PackageManager packageManager = this.getPackageManager();

  while (i.hasNext()) {
    ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
    try {
      CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
      info.processName, PackageManager.GET_META_DATA));
      Log.w("LABEL", c.toString());
    } catch (Exception e) {
        // Name Not FOund Exception
    }
  }

    ApplicationInfo applicationInfo = null;
    try {
      applicationInfo = packageManager.getApplicationInfo(packageName, 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
    Log.d("Notifications", "app name is: " + applicationLabel);

    if (applicationLabel.toString().equals("Chrome")) {
      String capturedUrl = captureUrl(parentNodeInfo);
      if (capturedUrl == null) {
        return;
      }
  
      AccessibilityServiceModule.prepareEventFromChromeURL(capturedUrl);
      AccessibilityServiceModule.prepareEventFromAppNameFocus(applicationLabel.toString());
      return;
    }
    // AccessibilityServiceModule.prepareEventFromChromeURL("");
    AccessibilityServiceModule.prepareEventFromAppNameFocus(applicationLabel.toString());
  }

  @Override
  public void onInterrupt() {
    AccessibilityServiceModule.prepareEventFromLeavingChromeApp("true");
  }
}