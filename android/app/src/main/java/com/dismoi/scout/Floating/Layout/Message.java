package com.dismoi.scout.Floating.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;

public class Message extends Base {
  private final WindowManager windowManager;

  public Message(Context context, AttributeSet attrs) {
    super(context, attrs);
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  }
}
