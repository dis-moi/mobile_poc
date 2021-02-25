package com.mobile_poc;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nullable;

public class AccessibilityServiceForChromeURLModule extends ReactContextBaseJavaModule {

  private static ReactApplicationContext reactContext;

  public AccessibilityServiceForChromeURLModule(ReactApplicationContext reactContext) { 
    super(reactContext);
    
    AccessibilityServiceForChromeURLModule.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "AccessibilityServiceForChromeURL";
  }

  @ReactMethod
  public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
    callback.invoke("Success! Received number: " + numberArgument + " string: " + stringArgument);
  }

  private static void sendEvent(
    ReactContext reactContext,
    String eventName,
    @Nullable String params
  ) {

    if (reactContext == null) {
      return;
    }

    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  public static void prepareEvent(String params) {
    sendEvent(reactContext, "EventFromChromeURLNativeModule", params);
  }
}
