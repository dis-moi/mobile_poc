package com.dismoi.scout.Floating;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.dismoi.scout.Floating.Layout.Bubble;
import com.dismoi.scout.Floating.Layout.Message;
import com.dismoi.scout.Floating.OnCallback;
import com.dismoi.scout.R;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.annotation.SuppressLint;

public class FloatingModule extends ReactContextBaseJavaModule {

  private Manager bubblesManager;
  private Manager messagesManager;
  private final ReactApplicationContext reactContext;
  private Bubble bubbleDisMoiView;
  private Message messageDisMoiView;

  public FloatingModule(ReactApplicationContext reactContext) {
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

  @NonNull
  @Override
  public String getName() {
    return "FloatingModule";
  }

  @ReactMethod
  public void showFloatingDisMoiBubble(int x, int y, final Promise promise) {
    try {
      this.addNewFloatingDisMoiBubble(x, y);
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod
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

  @ReactMethod
  public void hideFloatingDisMoiBubble(final Promise promise) {
    try {
      this.removeDisMoiBubble();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod
  public void hideFloatingDisMoiMessage(final Promise promise) {
    try {
      this.removeDisMoiMessage();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }
  
  @ReactMethod
  public void requestPermission(final Promise promise) {
    this.requestPermissionAction(promise);
  }  
  
  @ReactMethod
  public void checkPermission(final Promise promise) {
    try {
      promise.resolve(hasPermission());
    } catch (Exception e) {
      promise.reject("");
    }
  }  
  
  @ReactMethod
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

    messageDisMoiView = (Message) LayoutInflater.from(reactContext).inflate(R.layout.dismoi_message, messageDisMoiView, false);

    ImageButton imageButton = (ImageButton) messageDisMoiView.findViewById(R.id.close);
    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendEventToReactNative("floating-dismoi-message-press");
      }
    });

    messagesManager.addDisMoiMessage(messageDisMoiView, x, y);
  }

  private void addNewFloatingDisMoiBubble(int x, int y) {

    bubbleDisMoiView = (Bubble) LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, bubbleDisMoiView, false);

    bubbleDisMoiView.setOnBubbleRemoveListener(new Bubble.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(Bubble bubble) {
        bubbleDisMoiView = null;
        sendEventToReactNative("floating-dismoi-bubble-remove");
      }
    });
    bubbleDisMoiView.setOnBubbleClickListener(new Bubble.OnBubbleClickListener() {
      @Override
      public void onBubbleClick(Bubble bubble) {
        sendEventToReactNative("floating-dismoi-bubble-press");
      }
    });
    bubbleDisMoiView.setShouldStickToWall(true);
    bubblesManager.addDisMoiBubble(bubbleDisMoiView, x, y);
  }

  private boolean hasPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return Settings.canDrawOverlays(reactContext);
    }
    return true;
  }

  private void removeDisMoiBubble() {
    if (bubbleDisMoiView != null) {
      bubblesManager.removeBubble(bubbleDisMoiView);
    }
  }

  private void removeDisMoiMessage() {
    if (messageDisMoiView != null) {
      messagesManager.removeDisMoiMessage(messageDisMoiView);
    }
  }

  public void requestPermissionAction(final Promise promise) {
    if (!hasPermission()) {
      Intent intent = null;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactContext.getPackageName()));
      }
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

    messagesManager = new Manager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {}
    }).build();

    messagesManager.initialize();
  }

  private void initializeBubblesManager() {

    bubblesManager = new Manager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout).setInitializationCallback(new OnCallback() {
      @Override
      public void onInitialized() {}
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