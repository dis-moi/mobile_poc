package com.dismoi.scout;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

import com.dismoi.scout.FloatingTrashLayout;

final class FloatingLayoutCoordinator {
  private static FloatingLayoutCoordinator INSTANCE;
  private FloatingTrashLayout trashView;
  private WindowManager windowManager;
  private FloatingService bubblesService;

  private static FloatingLayoutCoordinator getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FloatingLayoutCoordinator();
    }
    return INSTANCE;
  }

  private FloatingLayoutCoordinator() { }

  public void notifyBubblePositionChanged(FloatingLayout bubble, int x, int y) {
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

  private void applyTrashMagnetismToBubble(FloatingLayout bubble) {
    View trashContentView = getTrashContent();
    int trashCenterX = (trashContentView.getLeft() + (trashContentView.getMeasuredWidth() / 2));
    int trashCenterY = (trashContentView.getTop() + (trashContentView.getMeasuredHeight() / 2));
    int x = (trashCenterX - (bubble.getMeasuredWidth() / 2));
    int y = (trashCenterY - (bubble.getMeasuredHeight() / 2));
    bubble.getViewParams().x = x;
    bubble.getViewParams().y = y;
    windowManager.updateViewLayout(bubble, bubble.getViewParams());
  }

  private boolean checkIfBubbleIsOverTrash(FloatingLayout bubble) {
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

  public void notifyBubbleRelease(FloatingLayout bubble) {
    if (trashView != null) {
      if (checkIfBubbleIsOverTrash(bubble)) {
        bubblesService.removeBubble(bubble);
      }
      trashView.setVisibility(View.GONE);
    }
  }

  public static class Builder {
    private FloatingLayoutCoordinator layoutCoordinator;

    public Builder(FloatingService service) {
      layoutCoordinator = getInstance();
      layoutCoordinator.bubblesService = service;
    }

    public Builder setTrashView(FloatingTrashLayout trashView) {
      layoutCoordinator.trashView = trashView;
      return this;
    }

    public Builder setWindowManager(WindowManager windowManager) {
      layoutCoordinator.windowManager = windowManager;
      return this;
    }

    public FloatingLayoutCoordinator build() {
      return layoutCoordinator;
    }
  }

  private View getTrashContent() {
    return trashView.getChildAt(0);
  }
}
