package com.dismoi.scout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class FloatingManager {
  private static FloatingManager INSTANCE;
  private Context context;
  private boolean bounded;
  private FloatingService bubblesService;
  private int trashLayoutResourceId;
  private OnCallback listener;

  private static FloatingManager getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new FloatingManager(context);
    }
    return INSTANCE;
  }

  private ServiceConnection disMoiServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      FloatingService.FloatingServiceBinder binder = (FloatingService.FloatingServiceBinder)service;
      FloatingManager.this.bubblesService = binder.getService();
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

  private FloatingManager(Context context) {
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

  public void addBubble(FloatingLayout bubble, int x, int y) {
    if (bounded) {
      bubblesService.addBubble(bubble, x, y);
    }
  }

  public void addDisMoiMessage(FloatingMessageLayout bubble, int x, int y) {
    if (bounded) {
      bubblesService.addDisMoiMessage(bubble, x, y);
    }
  }

  public void removeBubble(FloatingLayout bubble) {
    if (bounded) {
      bubblesService.removeBubble(bubble);
    }
  }

  public void removeDisMoiMessage(FloatingMessageLayout message) {
    if (bounded) {
      bubblesService.removeMessage(message);
    }
  }

  public static class Builder {
    private FloatingManager disMoiManager;

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

    public FloatingManager build() {
      return disMoiManager;
    }
  }
}
