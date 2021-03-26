package com.dismoi.scout.Floating.Layout;

import android.view.View;
import android.view.WindowManager;

import com.dismoi.scout.Floating.Layout.Trash;
import com.dismoi.scout.Floating.FloatingService;

final public class Coordinator {
  private static Coordinator INSTANCE;
  private Trash trashView;
  private WindowManager windowManager;
  private FloatingService bubblesService;

  private static Coordinator getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Coordinator();
    }
    return INSTANCE;
  }

  private Coordinator() { }

  public void notifyBubblePositionChanged(Bubble bubble, int x, int y) {
    if (trashView != null) {
      trashView.setVisibility(View.VISIBLE);
      if (checkIfBubbleIsOverTrash(bubble)) {
        trashView.applyMagnetism();
        trashView.vibrate();
        applyTrashMagnetismToBubble(bubble);
      } else {
        trashView.releaseMagnetism();
      }
    }
  }

  private void applyTrashMagnetismToBubble(Bubble bubble) {
    View trashContentView = getTrashContent();
    int trashCenterX = (trashContentView.getLeft() + (trashContentView.getMeasuredWidth() / 2));
    int trashCenterY = (trashContentView.getTop() + (trashContentView.getMeasuredHeight() / 2));
    int x = (trashCenterX - (bubble.getMeasuredWidth() / 2));
    int y = (trashCenterY - (bubble.getMeasuredHeight() / 2));
    bubble.getViewParams().x = x;
    bubble.getViewParams().y = y;
    windowManager.updateViewLayout(bubble, bubble.getViewParams());
  }

  private boolean checkIfBubbleIsOverTrash(Bubble bubble) {
    boolean result = false;
    if (trashView.getVisibility() == View.VISIBLE) {
      View trashContentView = getTrashContent();
      int trashWidth = trashContentView.getMeasuredWidth();
      int trashHeight = trashContentView.getMeasuredHeight();
      int trashLeft = (trashContentView.getLeft() - (trashWidth / 2));
      int trashRight = (trashContentView.getLeft() + trashWidth + (trashWidth / 2));
      int trashTop = (trashContentView.getTop() - (trashHeight / 2));
      int trashBottom = (trashContentView.getTop() + trashHeight + (trashHeight / 2));
      int bubbleWidth = bubble.getMeasuredWidth();
      int bubbleHeight = bubble.getMeasuredHeight();
      int bubbleLeft = bubble.getViewParams().x;
      int bubbleRight = bubbleLeft + bubbleWidth;
      int bubbleTop = bubble.getViewParams().y;
      int bubbleBottom = bubbleTop + bubbleHeight;
      if (bubbleLeft >= trashLeft && bubbleRight <= trashRight) {
        if (bubbleTop >= trashTop && bubbleBottom <= trashBottom) {
          result = true;
        }
      }
    }
    return result;
  }

  public void notifyBubbleRelease(Bubble bubble) {
    if (trashView != null) {
      if (checkIfBubbleIsOverTrash(bubble)) {
        bubblesService.removeBubble(bubble);
      }
      trashView.setVisibility(View.GONE);
    }
  }

  public static class Builder {
    private final Coordinator layoutCoordinator;

    public Builder(FloatingService service) {
      layoutCoordinator = getInstance();
      layoutCoordinator.bubblesService = service;
    }

    public Builder setTrashView(Trash trashView) {
      layoutCoordinator.trashView = trashView;
      return this;
    }

    public Builder setWindowManager(WindowManager windowManager) {
      layoutCoordinator.windowManager = windowManager;
      return this;
    }

    public Coordinator build() {
      return layoutCoordinator;
    }
  }

  private View getTrashContent() {
    return trashView.getChildAt(0);
  }
}
