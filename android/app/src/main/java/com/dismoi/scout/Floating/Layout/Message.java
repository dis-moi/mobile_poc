package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Message extends Base {
  private float initialTouchX;
  private float initialTouchY;
  private int initialX;
  private int initialY;
  private OnBubbleRemoveListener onBubbleRemoveListener;
  private OnBubbleClickListener onBubbleClickListener;
  private static final int TOUCH_TIME_THRESHOLD = 150;
  private long lastTouchDown;
  private int width;
  private final WindowManager windowManager;
  private boolean shouldStickToWall = true;

  public void setOnBubbleRemoveListener(OnBubbleRemoveListener listener) {
    onBubbleRemoveListener = listener;
  }

  public void setOnBubbleClickListener(OnBubbleClickListener listener) {
    onBubbleClickListener = listener;
  }

  public Message(Context context) {
    super(context);

    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public Message(Context context, AttributeSet attrs) {
    super(context, attrs);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public Message(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
  }

  public void setShouldStickToWall(boolean shouldStick) {
    this.shouldStickToWall = shouldStick;
  }

  public void notifyBubbleRemoved() {
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
    void onBubbleRemoved(Message bubble);
  }

  public interface OnBubbleClickListener {
    void onBubbleClick(Message bubble);
  }

  public void goToWall() {
    if (shouldStickToWall){
      int middle = width / 2;
      float nearestXWall = getViewParams().x >= middle ? width : 0;
    }
  }
}
