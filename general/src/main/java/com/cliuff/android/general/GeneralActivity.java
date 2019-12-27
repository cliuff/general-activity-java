package com.cliuff.android.general;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(GeneralActivity.WindowInsets) consumeInsets} 方法
 */
public abstract class GeneralActivity extends AppCompatActivity {

    public static class WindowInsets {
        public int left;
        public int top;
        public int right;
        public int bottom;

        public WindowInsets(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public WindowInsets(Rect rect) {
            this(rect.left, rect.top, rect.right, rect.bottom);
        }

        public WindowInsets(android.view.WindowInsets insets) {
            this(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }
    }

    private GeneralViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyEdgeToEdge();
        mViewModel = new ViewModelProvider(this).get(GeneralViewModel.class);
        mViewModel.windowInsets.observe(this, rect -> {
            if (rect == null) return;
            consumeInsets(rect);
        });
        applyInsets();
    }

    /**
     * 实现界面能够显示在状态栏之下
     */
    private void applyEdgeToEdge(){
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(decor.getSystemUiVisibility() | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获得状态栏高度、导航栏高度等
     */
    private void applyInsets(){
        View root = getWindow().getDecorView().getRootView();
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            mViewModel.windowInsets.setValue(new GeneralActivity.WindowInsets(insets));
            return insets;
        });
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(GeneralActivity.WindowInsets insets);
}
