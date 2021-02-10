package com.mobile_poc;

import com.mobile_poc.AccessibilityServiceForChromeURLModule;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityServiceInfo;

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

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    AccessibilityServiceForChromeURLModule.prepareEvent("The user is doing something with the chrome app");
  }

  @Override
  public void onInterrupt() {

  }

}
