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
import android.widget.ImageButton;

public class FloatingLayoutModule extends ReactContextBaseJavaModule {

  private FloatingManager bubblesManager;
  private FloatingManager messagesManager;
  private final ReactApplicationContext reactContext;
  private FloatingLayout bubbleDisMoiView;
  private FloatingMessageLayout messageDisMoiView;

  public FloatingLayoutModule(ReactApplicationContext reactContext) {
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
    return "FloatingLayout";
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
      this.removeDisMoiBubble();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void hideFloatingDisMoiMessage(final Promise promise) {
    try {
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
      this.initializeBubblesManager();
      this.initializeDisMoiMessageManager();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  private void addNewFloatingDisMoiMessage(int x, int y) {
    this.removeDisMoiBubble();

    messageDisMoiView = (FloatingMessageLayout) LayoutInflater.from(reactContext).inflate(R.layout.dismoi_message, null);

    ImageButton ib = (ImageButton) messageDisMoiView.findViewById(R.id.close);
    ib.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendEventToReactNative("floating-dismoi-message-press");
      }
    });

    messageDisMoiView.setOnBubbleRemoveListener(new FloatingMessageLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(FloatingMessageLayout bubble) {
        messageDisMoiView = null;
        sendEventToReactNative("floating-dismoi-message-remove");
      }
    });

    messagesManager.addDisMoiMessage(messageDisMoiView, x, y);
  }

  private void addNewFloatingDisMoiBubble(int x, int y) {
    
    bubbleDisMoiView = (FloatingLayout) LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, null);

    bubbleDisMoiView.setOnBubbleRemoveListener(new FloatingLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(FloatingLayout bubble) {
        bubbleDisMoiView = null;
        sendEventToReactNative("floating-dismoi-bubble-remove");
      }
    });
    bubbleDisMoiView.setOnBubbleClickListener(new FloatingLayout.OnBubbleClickListener() {
      @Override
      public void onBubbleClick(FloatingLayout bubble) {
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
    if (bubbleDisMoiView != null) {
      try {
        bubblesManager.removeBubble(bubbleDisMoiView);
      } catch(Exception e){

      }
    }
  }

  private void removeDisMoiMessage() {
    if (messageDisMoiView != null) {
      try {
        messagesManager.removeDisMoiMessage(messageDisMoiView);
      } catch(Exception e){

      }
    }
  }

  public void requestPermissionAction(final Promise promise) {
    if (!hasPermission()) {
      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactContext.getPackageName()));
      Bundle bundle = new Bundle();
      reactContext.startActivityForResult(intent, 0, bundle);
    } 
    if (hasPermission()) {
      promise.resolve("");
    } else {
      promise.reject("");
    }
  }

  private void initializeDisMoiMessageManager() {

    messagesManager = new FloatingManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {
        // addNewBubble();
      }
    }).build();

    messagesManager.initialize();
  }

  private void initializeBubblesManager() {

    bubblesManager = new FloatingManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {
        // addNewBubble();
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