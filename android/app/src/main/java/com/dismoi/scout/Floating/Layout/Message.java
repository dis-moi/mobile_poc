package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;

import com.dismoi.scout.R;

public class Message extends Layout {
  private final WindowManager windowManager;
  private final MoveAnimator animator;
  private Context c;
  private int width;

  public Message(Context context, AttributeSet attrs) {
    super(context, attrs);

    Log.v("Notifications", "Message view animator");

    animator = new MoveAnimator();
    c = context;
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  private void initializeView() {

    Log.v("Notifications", "initialize view");

    setClickable(true);
  }

  @Override
  protected void onAttachedToWindow() {

    Log.v("Notifications", "on attached to window");

    super.onAttachedToWindow();
    playAnimation();
  }

  private void playAnimation() {

    Log.v("Notifications", "play animation");
    AnimatorSet animator = (AnimatorSet) AnimatorInflater
              .loadAnimator(getContext(), R.animator.message_slide_up);
    animator.setTarget(this);
    animator.start();
  }

  private void move(float deltaX, float deltaY) {

    Log.v("Notifications", "move");

    getViewParams().x += deltaX;
    getViewParams().y += deltaY;
    windowManager.updateViewLayout(this, getViewParams());
  }

  private class MoveAnimator implements Runnable {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private float destinationX;
    private float destinationY;
    private long startingTime;

    private void start(float x, float y) {

      Log.v("Notifications", "start");

      this.destinationX = x;
      this.destinationY = y;
      startingTime = System.currentTimeMillis();
      handler.post(this);
    }

    @Override
    public void run() {
      Log.v("Notifications", "run");
      if (getRootView() != null && getRootView().getParent() != null) {
        float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
        float deltaX = (destinationX -  getViewParams().x) * progress;
        float deltaY = (destinationY -  getViewParams().y) * progress;
        move(deltaX, deltaY);
        if (progress < 1) {
          handler.post(this);
        }
      }
    }

    private void stop() {
      Log.v("Notifications", "stop");
      handler.removeCallbacks(this);
    }
  }

}
