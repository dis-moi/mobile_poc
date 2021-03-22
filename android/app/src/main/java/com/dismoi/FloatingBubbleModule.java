package com.dismoi.scout;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.util.Log;

import android.os.Bundle;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;

import android.app.Activity;

import com.dismoi.scout.DisMoiManager;
import com.dismoi.scout.DisMoiLayout;
import com.dismoi.scout.DisMoiMessageLayout;
import android.widget.ImageButton;
import com.dismoi.scout.OnCallback;


public class FloatingBubbleModule extends ReactContextBaseJavaModule {

  private DisMoiManager bubblesManager;
  private DisMoiManager disMoiManager;

  private final ReactApplicationContext reactContext;
  private DisMoiLayout bubbleDisMoiView;
  private DisMoiMessageLayout messageDisMoiView;

  public FloatingBubbleModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @ReactMethod
  public void reopenApp(){
    Intent launchIntent = reactContext.getPackageManager().getLaunchIntentForPackage(reactContext.getPackageName());
    if (launchIntent != null) {
      reactContext.startActivity(launchIntent);
    }
  }

  @Override
  public String getName() {
    return "FloatingBubble";
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void showFloatingDisMoiBubble(int x, int y, final Promise promise) {
    try {
      this.addNewFloatingDisMoiBubble(x, y);
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void showFloatingDisMoiMessage(int x, int y, final Promise promise) {
    if (messageDisMoiView == null) {
      try {
        
        this.addNewFloatingDisMoiMessage(x, y);
        promise.resolve("");
      } catch (Exception e) {
        promise.reject("");
      }
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void hideFloatingDisMoiBubble(final Promise promise) {
    try {
      Log.d("Notification", "hideFloatingBubble");

      this.removeDisMoiBubble();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void hideFloatingDisMoiMessage(final Promise promise) {
    try {
      Log.d("Notification", "hideFloatingDisMoiMessage");

      this.removeDisMoiMessage();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }
  
  @ReactMethod // Notates a method that should be exposed to React
  public void requestPermission(final Promise promise) {
    try {
      this.requestPermissionAction(promise);
    } catch (Exception e) {
    }
  }  
  
  @ReactMethod // Notates a method that should be exposed to React
  public void checkPermission(final Promise promise) {
    try {
      promise.resolve(hasPermission());
    } catch (Exception e) {
      promise.reject("");
    }
  }  
  
  @ReactMethod // Notates a method that should be exposed to React
  public void initialize(final Promise promise) {
    try {
      Log.d("Notification", "initialize");
      this.initializeBubblesManager();
      this.initializeDisMoiMessageManager();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  private void addNewFloatingDisMoiMessage(int x, int y) {
    this.removeDisMoiBubble();

   

    messageDisMoiView = (DisMoiMessageLayout) LayoutInflater.from(reactContext).inflate(R.layout.dismoi_message, null);

    ImageButton ib = (ImageButton) messageDisMoiView.findViewById(R.id.close);
    ib.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          Log.d("Notification", "**************************** dismoi click *********************************");
          sendEventToReactNative("floating-dismoi-message-press");
        }
    });

    messageDisMoiView.setOnBubbleRemoveListener(new DisMoiMessageLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(DisMoiMessageLayout bubble) {
        messageDisMoiView = null;
        sendEventToReactNative("floating-dismoi-message-remove");
      }
    });
    // messageDisMoiView.setShouldStickToWall(true);

    Log.d("Notification", "disMoiManager.addBubble");
    disMoiManager.addDisMoiMessage(messageDisMoiView, x, y);

  }

  private void addNewFloatingDisMoiBubble(int x, int y) {
    
    bubbleDisMoiView = (DisMoiLayout) LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, null);

    Log.d("Notification", "after add bubble");

    bubbleDisMoiView.setOnBubbleRemoveListener(new DisMoiLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(DisMoiLayout bubble) {
        bubbleDisMoiView = null;
        sendEventToReactNative("floating-dismoi-bubble-remove");
      }
    });
    bubbleDisMoiView.setOnBubbleClickListener(new DisMoiLayout.OnBubbleClickListener() {

      @Override
      public void onBubbleClick(DisMoiLayout bubble) {
        sendEventToReactNative("floating-dismoi-bubble-press");
      }
    });
    bubbleDisMoiView.setShouldStickToWall(true);
    bubblesManager.addBubble(bubbleDisMoiView, x, y);
  }

  private boolean hasPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return Settings.canDrawOverlays(reactContext);
    }
    return true;
  }

  private void removeDisMoiBubble() {
    Log.d("Notification", "removeBubble");

    if (bubbleDisMoiView != null) {

      Log.d("Notification", "inside remove Bubble");
      try {
        bubblesManager.removeBubble(bubbleDisMoiView);
      } catch(Exception e){

      }
    }
  }

  private void removeDisMoiMessage() {
    Log.d("Notification", "remove Bubble message");

    if (messageDisMoiView != null) {
      Log.d("Notification", "inside remove Bubble");
      try {
        disMoiManager.removeDisMoiMessage(messageDisMoiView);
      } catch(Exception e){

      }
    }
  }

  public void requestPermissionAction(final Promise promise) {

    Log.d("Notification", "request permission");

    if (!hasPermission()) {
      Log.d("Notification", "has not permission");

      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactContext.getPackageName()));
      Bundle bundle = new Bundle();
      reactContext.startActivityForResult(intent, 0, bundle);
    } 
    if (hasPermission()) {
      Log.d("Notification", "has permission");
      promise.resolve("");
    } else {
      promise.reject("");
    }
  }

  private void initializeDisMoiMessageManager() {

    disMoiManager = new DisMoiManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {
        // addNewBubble();
        Log.d("Notification", "*************************** PLEASE **********************************");
      }
    }).build();

    disMoiManager.initialize();
  }

  private void initializeBubblesManager() {

    Log.d("Notification", "initialize bubble manager");

    bubblesManager = new DisMoiManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {
        // addNewBubble();
        Log.d("Notification", "First bubble");

        
      }
    }).build();

    bubblesManager.initialize();
  }

  private void sendEventToReactNative(String eventName) {
    WritableMap params = Arguments.createMap();
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }
}