package com.dismoi.scout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

class DisMoiBaseLayout extends FrameLayout {
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private DisMoiLayoutCoordinator layoutCoordinator;

    void setLayoutCoordinator(DisMoiLayoutCoordinator layoutCoordinator) {
        this.layoutCoordinator = layoutCoordinator;
    }

    DisMoiLayoutCoordinator getLayoutCoordinator() {
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

    public DisMoiBaseLayout(Context context) {
        super(context);
    }

    public DisMoiBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisMoiBaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
