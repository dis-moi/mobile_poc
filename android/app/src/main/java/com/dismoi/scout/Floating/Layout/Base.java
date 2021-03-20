package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class Base extends FrameLayout {
  private WindowManager windowManager;
  private WindowManager.LayoutParams params;
  private Coordinator layoutCoordinator;

  public void setLayoutCoordinator(Coordinator layoutCoordinator) {
    this.layoutCoordinator = layoutCoordinator;
  }

  Coordinator getLayoutCoordinator() {
    return layoutCoordinator;
  }

  public void setWindowManager(WindowManager windowManager) {
    this.windowManager = windowManager;
  }

  WindowManager getWindowManager() {
    return this.windowManager;
  }

  public void setViewParams(WindowManager.LayoutParams params) {
      this.params = params;
  }

  public WindowManager.LayoutParams getViewParams() {
    return this.params;
  }

  public Base(Context context) {
    super(context);
  }

  public Base(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public Base(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
