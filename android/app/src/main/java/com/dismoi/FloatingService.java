package com.dismoi.scout;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import com.dismoi.scout.FloatingLayout;
import com.dismoi.scout.FloatingTrashLayout;
import com.dismoi.scout.FloatingLayoutCoordinator;
import android.util.Log;
import android.widget.Button;

public class FloatingService extends Service {
  private FloatingServiceBinder binder = new FloatingServiceBinder();
  private List<FloatingLayout> bubbles = new ArrayList<>();
  private List<FloatingMessageLayout> messages = new ArrayList<>();
  private FloatingTrashLayout bubblesTrash;
  private WindowManager windowManager;
  private FloatingLayoutCoordinator layoutCoordinator;

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    for (FloatingLayout bubble : bubbles) {
      recycleBubble(bubble);
    }
    bubbles.clear();
    return super.onUnbind(intent);
  }

  private void recycleBubble(final FloatingLayout bubble) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().removeView(bubble);
        for (FloatingLayout cachedBubble : bubbles) {
          if (cachedBubble == bubble) {
            bubble.notifyBubbleRemoved();
            bubbles.remove(cachedBubble);
            break;
          }
        }
      }
    });
  }

  private void recycleMessage(final FloatingMessageLayout message) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().removeView(message);
        for (FloatingMessageLayout cachedMessage : messages) {
          if (cachedMessage == message) {
            message.notifyBubbleRemoved();
            messages.remove(cachedMessage);
            break;
          }
        }
      }
    });
  }

  private WindowManager getWindowManager() {
    if (windowManager == null) {
      windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
    }
    return windowManager;
  }

  public void addBubble(FloatingLayout bubble, int x, int y) {
    WindowManager.LayoutParams layoutParams = buildLayoutParamsForBubble(x, y);
    bubble.setWindowManager(getWindowManager());
    bubble.setViewParams(layoutParams);
    bubble.setLayoutCoordinator(layoutCoordinator);
    bubbles.add(bubble);
    addViewToWindow(bubble);
  }

  public void addDisMoiMessage(FloatingMessageLayout bubble, int x, int y) {
    WindowManager.LayoutParams layoutParams = buildLayoutParamsForMessage(x, y);
    bubble.setWindowManager(getWindowManager());
    bubble.setViewParams(layoutParams);
    bubble.setLayoutCoordinator(layoutCoordinator);
    messages.add(bubble);
    addViewToWindow(bubble);
  }

  void addTrash(int trashLayoutResourceId) {
    if (trashLayoutResourceId != 0) {
      bubblesTrash = new FloatingTrashLayout(this);
      bubblesTrash.setWindowManager(windowManager);
      bubblesTrash.setViewParams(buildLayoutParamsForTrash());
      bubblesTrash.setVisibility(View.GONE);
      LayoutInflater.from(this).inflate(trashLayoutResourceId, bubblesTrash, true);
      addViewToWindow(bubblesTrash);
      initializeLayoutCoordinator();
    }
  }

  private void initializeLayoutCoordinator() {
    layoutCoordinator = new FloatingLayoutCoordinator.Builder(this)
            .setWindowManager(getWindowManager())
            .setTrashView(bubblesTrash)
            .build();
  }

  private void addViewToWindow(final FloatingBaseLayout view) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().addView(view, view.getViewParams());
      }
    });
  }

  private WindowManager.LayoutParams buildLayoutParamsForBubble(int x, int y) {
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT);
    params.gravity = Gravity.TOP | Gravity.START;
    params.x = x;
    params.y = y;
    return params;
  }

  private WindowManager.LayoutParams buildLayoutParamsForMessage(int x, int y) {
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT);
    params.gravity = Gravity.TOP | Gravity.START;
    params.x = x;
    params.y = y;
    return params;
  }

  private WindowManager.LayoutParams buildLayoutParamsForTrash() {
    int x = 0;
    int y = 0;
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT);
    params.x = x;
    params.y = y;
    return params;
  }

  public void removeBubble(FloatingLayout bubble) {
    recycleBubble(bubble);
  }

  public void removeMessage(FloatingMessageLayout message) {
    recycleMessage(message);
  }

  public class FloatingServiceBinder extends Binder {
    public FloatingService getService() {
      return FloatingService.this;
    }
  }
}