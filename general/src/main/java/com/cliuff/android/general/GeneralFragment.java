package com.cliuff.android.general;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * 实现状态栏透明
 * 使用方法：继承这个类并实现 {@link #consumeInsets(GeneralActivity.WindowInsets) consumeInsets} 方法
 */
public abstract class GeneralFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (!(activity instanceof GeneralActivity)) return;
        GeneralViewModel generalViewModel = new ViewModelProvider(activity).get(GeneralViewModel.class);
        generalViewModel.windowInsets.observe(getViewLifecycleOwner(), rect -> {
            if (rect == null) return;
            consumeInsets(rect);
        });
    }

    /**
     * 响应窗口 insets
     */
    protected abstract void consumeInsets(GeneralActivity.WindowInsets insets);
}
