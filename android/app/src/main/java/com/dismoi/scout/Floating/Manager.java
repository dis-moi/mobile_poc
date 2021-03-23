package com.dismoi.scout.Floating;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.Layout;

import com.dismoi.scout.Floating.FloatingService;
import com.dismoi.scout.Floating.OnCallback;

import com.dismoi.scout.Floating.Layout.Bubble;
import com.dismoi.scout.Floating.Layout.Message;

public class Manager {
  private static Manager INSTANCE;
  private final Context context;
  private boolean bounded;
  private FloatingService bubblesService;
  private int trashLayoutResourceId;
  private OnCallback listener;

  private static Manager getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new Manager(context);
    }
    return INSTANCE;
  }

  private final ServiceConnection disMoiServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      FloatingService.FloatingServiceBinder binder = (FloatingService.FloatingServiceBinder)service;
      Manager.this.bubblesService = binder.getService();
      configureBubblesService();
      bounded = true;
      if (listener != null) {
        listener.onInitialized();
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      bounded = false;
    }
  };

  private Manager(Context context) {
    this.context = context;
  }

  private void configureBubblesService() {
    bubblesService.addTrash(trashLayoutResourceId);
  }

  public void initialize() {
    context.bindService(new Intent(context, FloatingService.class),
            disMoiServiceConnection,
            Context.BIND_AUTO_CREATE);
  }

  public void recycle() {
    context.unbindService(disMoiServiceConnection);
  }

  public void addDisMoiBubble(Bubble bubble, int x, int y) {
    if (bounded) {
      bubblesService.addDisMoiBubble(bubble, x, y);
    }
  }

  public void addDisMoiMessage(Message message, int x, int y) {
    if (bounded) {
      bubblesService.addDisMoiMessage(message, x, y);
    }
  }

  public void removeBubble(Bubble bubble) {
    if (bounded) {
      bubblesService.removeBubble(bubble);
    }
  }

  public void removeDisMoiMessage(Message message) {
    if (bounded) {
      bubblesService.removeMessage(message);
    }
  }

  public static class Builder {
    private final Manager disMoiManager;

    public Builder(Context context) {
      this.disMoiManager = getInstance(context);
    }

    public Builder setInitializationCallback(OnCallback listener) {
      disMoiManager.listener = listener;
      return this;
    }

    public Builder setTrashLayout(int trashLayoutResourceId) {
      disMoiManager.trashLayoutResourceId = trashLayoutResourceId;
      return this;
    }

    public Manager build() {
      return disMoiManager;
    }
  }
}
