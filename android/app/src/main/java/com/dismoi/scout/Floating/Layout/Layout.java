package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class Layout extends FrameLayout {
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

  public void create(WindowManager getWindowManager, WindowManager.LayoutParams layoutParams, Coordinator layoutCoordinator) {
    setWindowManager(getWindowManager);
    setViewParams(layoutParams);
    setLayoutCoordinator(layoutCoordinator);
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

  public Layout(Context context) {
    super(context);
  }

  public Layout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public Layout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
