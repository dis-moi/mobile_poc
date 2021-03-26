package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Message extends Base {
  private OnBubbleRemoveListener onBubbleRemoveListener;
  private OnBubbleClickListener onBubbleClickListener;
  private int width;
  private final WindowManager windowManager;

  public void setOnBubbleRemoveListener(OnBubbleRemoveListener listener) {
    onBubbleRemoveListener = listener;
  }

  public void setOnBubbleClickListener(OnBubbleClickListener listener) {
    onBubbleClickListener = listener;
  }

  public Message(Context context, AttributeSet attrs) {
    super(context, attrs);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    initializeView();
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

  public interface OnBubbleRemoveListener {
    void onBubbleRemoved(Message bubble);
  }

  public interface OnBubbleClickListener {
    void onBubbleClick(Message bubble);
  }
}
