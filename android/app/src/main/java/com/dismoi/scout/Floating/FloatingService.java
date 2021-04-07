package com.dismoi.scout.Floating;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.dismoi.scout.Floating.Layout.Bubble;
import com.dismoi.scout.Floating.Layout.Coordinator;
import com.dismoi.scout.Floating.Layout.Layout;
import com.dismoi.scout.Floating.Layout.Message;
import com.dismoi.scout.Floating.Layout.Trash;

import java.util.ArrayList;
import java.util.List;

public class FloatingService extends Service {
  private final FloatingServiceBinder binder = new FloatingServiceBinder();
  private final List<Bubble> bubbles = new ArrayList<>();
  private final List<Message> messages = new ArrayList<>();
  private Trash bubblesTrash;
  private WindowManager windowManager;
  private Coordinator layoutCoordinator;
  private Bubble bubble;
  private Message message;

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    for (Bubble bubble : bubbles) {
      recycleBubble(bubble);
    }
    bubbles.clear();
    return super.onUnbind(intent);
  }

  private void recycleBubble(final Bubble bubble) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().removeView(bubble);
        for (Bubble cachedBubble : bubbles) {
          if (cachedBubble == bubble) {
            bubble.notifyBubbleRemoved();
            bubbles.remove(cachedBubble);
            break;
          }
        }
      }
    });
  }

  private void recycleMessage(final Message message) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().removeView(message);
        for (Message cachedMessage : messages) {
          if (cachedMessage == message) {
            messages.remove(cachedMessage);
            break;
          }
        }
      }
    });
  }

  private WindowManager getWindowManager() {
    if (windowManager == null) {
      windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }
    return windowManager;
  }

  public void addDisMoiBubble(Bubble bubble, int x, int y) {
    WindowManager.LayoutParams layoutParams = buildLayoutParamsForBubble(x, y);
    bubble.create(getWindowManager(), layoutParams, layoutCoordinator);
    bubbles.add(bubble);
    addViewToWindow(bubble);
  }

  public void addDisMoiMessage(Message message, int x, int y) {
    WindowManager.LayoutParams layoutParams = buildLayoutParamsForMessage(x, y);
    message.create(getWindowManager(), layoutParams, layoutCoordinator);
    messages.add(message);
    
    addViewToWindowForMessage(message);
  }

  void addTrash(int trashLayoutResourceId) {
    if (trashLayoutResourceId != 0) {
      bubblesTrash = new Trash(this);
      bubblesTrash.setWindowManager(windowManager);
      bubblesTrash.setViewParams(buildLayoutParamsForTrash());
      bubblesTrash.setVisibility(View.GONE);
      LayoutInflater.from(this).inflate(trashLayoutResourceId, bubblesTrash, true);
      addViewToWindow(bubblesTrash);
      initializeLayoutCoordinator();
    }
  }

  private void initializeLayoutCoordinator() {
    layoutCoordinator = new Coordinator.Builder(this)
            .setWindowManager(getWindowManager())
            .setTrashView(bubblesTrash)
            .build();
  }

  private void addViewToWindow(final Layout view) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        getWindowManager().addView(view, view.getViewParams());
      }
    });
  }

  private void addViewToWindowForMessage(final Layout view) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        
        getWindowManager().addView(view, view.getViewParams());
      }
    });
  }

  private WindowManager.LayoutParams buildLayoutParamsForBubble(int x, int y) {
    WindowManager.LayoutParams params = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      params = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSPARENT);
    }
    params.gravity = Gravity.TOP | Gravity.START;
    params.x = x;
    params.y = y;
    return params;
  }

  private WindowManager.LayoutParams buildLayoutParamsForMessage(int x, int y) {
    WindowManager.LayoutParams params = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      params = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSPARENT);
    }
    params.gravity = Gravity.TOP | Gravity.START;
    params.x = x;
    params.y = y;
    return params;
  }

  private WindowManager.LayoutParams buildLayoutParamsForTrash() {
    int x = 0;
    int y = 0;
    WindowManager.LayoutParams params = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      params = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.MATCH_PARENT,
              WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSPARENT);
    }
    params.x = x;
    params.y = y;
    return params;
  }

  public void removeBubble(Bubble bubble) {
    recycleBubble(bubble);
  }

  public void removeMessage(Message message) {
    recycleMessage(message);
  }

  public class FloatingServiceBinder extends Binder {
    public FloatingService getService() {
      return FloatingService.this;
    }
  }
}