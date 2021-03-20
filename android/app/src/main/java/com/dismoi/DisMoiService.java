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

import com.dismoi.scout.DisMoiLayout;
import com.dismoi.scout.DisMoiTrashLayout;
import com.dismoi.scout.DisMoiLayoutCoordinator;
import android.util.Log;

public class DisMoiService extends Service {
    private DisMoiServiceBinder binder = new DisMoiServiceBinder();
    private List<DisMoiLayout> bubbles = new ArrayList<>();
    private DisMoiTrashLayout bubblesTrash;
    private WindowManager windowManager;
    private DisMoiLayoutCoordinator layoutCoordinator;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Notification", "[DisMoiService] On BIND");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        for (DisMoiLayout bubble : bubbles) {
            recycleBubble(bubble);
        }
        bubbles.clear();
        return super.onUnbind(intent);
    }

    private void recycleBubble(final DisMoiLayout bubble) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                getWindowManager().removeView(bubble);
                for (DisMoiLayout cachedBubble : bubbles) {
                    if (cachedBubble == bubble) {
                        bubble.notifyBubbleRemoved();
                        bubbles.remove(cachedBubble);
                        break;
                    }
                }
            }
        });
    }

    private WindowManager getWindowManager() {
        Log.d("Notification", "[DisMoiService] getWindowManager");
        if (windowManager == null) {
            Log.d("Notification", "[DisMoiService] getWindowService");

            windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        }
        return windowManager;
    }

    public void addBubble(DisMoiLayout bubble, int x, int y) {
        Log.d("Notification", "[DisMoiService] addBubble");
        WindowManager.LayoutParams layoutParams = buildLayoutParamsForBubble(x, y);
        bubble.setWindowManager(getWindowManager());
        bubble.setViewParams(layoutParams);
        bubble.setLayoutCoordinator(layoutCoordinator);
        bubbles.add(bubble);
        addViewToWindow(bubble);
    }

    void addTrash(int trashLayoutResourceId) {
        if (trashLayoutResourceId != 0) {
            bubblesTrash = new DisMoiTrashLayout(this);
            bubblesTrash.setWindowManager(windowManager);
            bubblesTrash.setViewParams(buildLayoutParamsForTrash());
            bubblesTrash.setVisibility(View.GONE);
            LayoutInflater.from(this).inflate(trashLayoutResourceId, bubblesTrash, true);
            addViewToWindow(bubblesTrash);
            initializeLayoutCoordinator();
        }
    }

    private void initializeLayoutCoordinator() {
        Log.d("Notification", "[DisMoiService] initializeLayoutCoordinator");
        layoutCoordinator = new DisMoiLayoutCoordinator.Builder(this)
                .setWindowManager(getWindowManager())
                .setTrashView(bubblesTrash)
                .build();
    }

    private void addViewToWindow(final DisMoiBaseLayout view) {
        Log.d("Notification", "[DisMoiService] add view to window");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("Notification", "[DisMoiService] run");

                getWindowManager().addView(view, view.getViewParams());
            }
        });
    }

    private WindowManager.LayoutParams buildLayoutParamsForBubble(int x, int y) {

        Log.d("Notification", "[DisMoiService] buildLayoutParamsForBubble");

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

    public void removeBubble(DisMoiLayout bubble) {
        Log.d("Notification", "[DisMoiService] removeBubble");
        recycleBubble(bubble);
    }

    public class DisMoiServiceBinder extends Binder {
        public DisMoiService getService() {
            Log.d("Notification", "[DisMoiService] get service");

            return DisMoiService.this;
        }
    }
}