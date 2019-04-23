package com.wram.myframeworkdemo.me;

import com.navigation.BaseActivity;
import com.navigation.BaseFragment;
import com.navigation.BaseViewModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/23 上午11:30
 */
public class MeVM extends BaseViewModel {
    public MeVM(@NotNull BaseActivity activity) {
        super(activity);
    }

    public MeVM(@NotNull BaseFragment fragment) {
        super(fragment);
    }
}
