package com.mobile_poc;

import com.mobile_poc.AccessibilityServiceForChromeURLModule;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityServiceInfo;

public class AccessibilityServiceForChromeURL extends AccessibilityService {

  @Override
  protected void onServiceConnected() {
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
