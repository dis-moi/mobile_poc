package com.dismoi.scout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

class FloatingBaseLayout extends FrameLayout {
  private WindowManager windowManager;
  private WindowManager.LayoutParams params;
  private FloatingLayoutCoordinator layoutCoordinator;

  void setLayoutCoordinator(FloatingLayoutCoordinator layoutCoordinator) {
    this.layoutCoordinator = layoutCoordinator;
  }

  FloatingLayoutCoordinator getLayoutCoordinator() {
    return layoutCoordinator;
  }

  void setWindowManager(WindowManager windowManager) {
    this.windowManager = windowManager;
  }

  WindowManager getWindowManager() {
    return this.windowManager;
  }

  void setViewParams(WindowManager.LayoutParams params) {
      this.params = params;
  }

  WindowManager.LayoutParams getViewParams() {
    return this.params;
  }

  public FloatingBaseLayout(Context context) {
    super(context);
  }

  public FloatingBaseLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FloatingBaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
