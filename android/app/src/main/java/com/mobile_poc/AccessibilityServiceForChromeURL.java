package com.mobile_poc;

import com.mobile_poc.AccessibilityServiceForChromeURLModule;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityServiceForChromeURL extends AccessibilityService {

  @Override
  protected void onServiceConnected() {
    AccessibilityServiceInfo info = getServiceInfo();
    info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
    info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;

    this.setServiceInfo(info);
    AccessibilityServiceForChromeURLModule.prepareEvent("Accessibility service has been activated by user");
  }

  @Override
  public void onDestroy() {
    AccessibilityServiceForChromeURLModule.prepareEvent("Accessibility service has been deactivated by user");
  }

  private String captureUrl(AccessibilityNodeInfo info) {
    List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar");
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

    String capturedUrl = captureUrl(parentNodeInfo);
    if (capturedUrl == null) {
      return;
    }

    AccessibilityServiceForChromeURLModule.prepareEvent(capturedUrl);
  }

  @Override
  public void onInterrupt() {
    AccessibilityServiceForChromeURLModule.prepareEvent("on interrupt");
  }
}
