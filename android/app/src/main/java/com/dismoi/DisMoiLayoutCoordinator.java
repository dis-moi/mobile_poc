package com.dismoi.scout;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

import com.dismoi.scout.DisMoiTrashLayout;

final class DisMoiLayoutCoordinator {
    private static DisMoiLayoutCoordinator INSTANCE;
    private DisMoiTrashLayout trashView;
    private WindowManager windowManager;
    private DisMoiService bubblesService;

    private static DisMoiLayoutCoordinator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DisMoiLayoutCoordinator();
        }
        return INSTANCE;
    }

    private DisMoiLayoutCoordinator() { }

    public void notifyBubblePositionChanged(DisMoiLayout bubble, int x, int y) {
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

    private void applyTrashMagnetismToBubble(DisMoiLayout bubble) {
        View trashContentView = getTrashContent();
        int trashCenterX = (trashContentView.getLeft() + (trashContentView.getMeasuredWidth() / 2));
        int trashCenterY = (trashContentView.getTop() + (trashContentView.getMeasuredHeight() / 2));
        int x = (trashCenterX - (bubble.getMeasuredWidth() / 2));
        int y = (trashCenterY - (bubble.getMeasuredHeight() / 2));
        bubble.getViewParams().x = x;
        bubble.getViewParams().y = y;
        windowManager.updateViewLayout(bubble, bubble.getViewParams());
    }

    private boolean checkIfBubbleIsOverTrash(DisMoiLayout bubble) {
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

    public void notifyBubbleRelease(DisMoiLayout bubble) {
        if (trashView != null) {
            if (checkIfBubbleIsOverTrash(bubble)) {
                bubblesService.removeBubble(bubble);
            }
            trashView.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private DisMoiLayoutCoordinator layoutCoordinator;

        public Builder(DisMoiService service) {
            layoutCoordinator = getInstance();
            layoutCoordinator.bubblesService = service;
        }

        public Builder setTrashView(DisMoiTrashLayout trashView) {
            layoutCoordinator.trashView = trashView;
            return this;
        }

        public Builder setWindowManager(WindowManager windowManager) {
            layoutCoordinator.windowManager = windowManager;
            return this;
        }

        public DisMoiLayoutCoordinator build() {
            return layoutCoordinator;
        }
    }

    private View getTrashContent() {
        return trashView.getChildAt(0);
    }
}
