package com.dismoi.scout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.dismoi.scout.DisMoiService;

public class DisMoiManager {
    private static DisMoiManager INSTANCE;
    private Context context;
    private boolean bounded;
    private DisMoiService bubblesService;
    private int trashLayoutResourceId;
    private OnCallback listener;

    private static DisMoiManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DisMoiManager(context);
        }
        return INSTANCE;
    }

    private ServiceConnection disMoiServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Notification", "On Service Connected");
            DisMoiService.DisMoiServiceBinder binder = (DisMoiService.DisMoiServiceBinder)service;
            DisMoiManager.this.bubblesService = binder.getService();
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

    private DisMoiManager(Context context) {
        this.context = context;
    }

    private void configureBubblesService() {
        bubblesService.addTrash(trashLayoutResourceId);
    }

    public void initialize() {
        Log.d("Notification", "[DisMoiManager] initialize");
        context.bindService(new Intent(context, DisMoiService.class),
                disMoiServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void recycle() {
        context.unbindService(disMoiServiceConnection);
    }

    public void addBubble(DisMoiLayout bubble, int x, int y) {
        Log.d("Notification", "[DisMoiManager] add bubble");
        if (bounded) {
            Log.d("Notification", "[DisMoiManager] inside bounded");
            bubblesService.addBubble(bubble, x, y);
        }
    }

    public void addDisMoiMessage(DisMoiMessageLayout bubble, int x, int y) {
        Log.d("Notification", "[DisMoiManager] add bubble");
        if (bounded) {
            Log.d("Notification", "[DisMoiManager] inside bounded");
            bubblesService.addDisMoiMessage(bubble, x, y);
        }
    }

    public void removeBubble(DisMoiLayout bubble) {
        if (bounded) {
            bubblesService.removeBubble(bubble);
        }
    }

    public void removeDisMoiMessage(DisMoiMessageLayout message) {
        if (bounded) {
            bubblesService.removeMessage(message);
        }
    }

    public static class Builder {
        private DisMoiManager disMoiManager;

        public Builder(Context context) {
            this.disMoiManager = getInstance(context);
        }

        public Builder setInitializationCallback(OnCallback listener) {
            Log.d("Notification", "[DisMoiManager] setInitializationCallback");

            disMoiManager.listener = listener;
            return this;
        }

        public Builder setTrashLayout(int trashLayoutResourceId) {
            disMoiManager.trashLayoutResourceId = trashLayoutResourceId;
            return this;
        }

        public DisMoiManager build() {
            return disMoiManager;
        }
    }
}
