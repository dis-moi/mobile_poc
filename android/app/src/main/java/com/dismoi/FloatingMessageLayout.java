package com.dismoi.scout;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.util.Log;

public class FloatingMessageLayout extends FloatingBaseLayout {
  private float initialTouchX;
  private float initialTouchY;
  private int initialX;
  private int initialY;
  private OnBubbleRemoveListener onBubbleRemoveListener;
  private OnBubbleClickListener onBubbleClickListener;
  private static final int TOUCH_TIME_THRESHOLD = 150;
  private long lastTouchDown;
  private int width;
  private WindowManager windowManager;
  private boolean shouldStickToWall = true;

  public void setOnBubbleRemoveListener(OnBubbleRemoveListener listener) {
    onBubbleRemoveListener = listener;
  }

  public void setOnBubbleClickListener(OnBubbleClickListener listener) {
    onBubbleClickListener = listener;
  }

  public FloatingMessageLayout(Context context) {
    super(context);

    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public FloatingMessageLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public FloatingMessageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public void setShouldStickToWall(boolean shouldStick) {
    this.shouldStickToWall = shouldStick;
  }

  void notifyBubbleRemoved() {
    if (onBubbleRemoveListener != null) {
      onBubbleRemoveListener.onBubbleRemoved(this);
    }
  }

  private void initializeView() {
    setClickable(true);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  private void updateSize() {
    DisplayMetrics metrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(metrics);
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    width = (size.x - this.getWidth());
  }

  public interface OnBubbleRemoveListener {
    void onBubbleRemoved(FloatingMessageLayout bubble);
  }

  public interface OnBubbleClickListener {
    void onBubbleClick(FloatingMessageLayout bubble);
  }

  public void goToWall() {
    if (shouldStickToWall){
      int middle = width / 2;
      float nearestXWall = getViewParams().x >= middle ? width : 0;
    }
  }
}
