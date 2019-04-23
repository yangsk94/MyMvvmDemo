package com.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.common.base.BaseConstraintLayout;
import com.wram.base_library.R;
import com.wram.base_library.databinding.RecyclerCommonBinding;
import org.jetbrains.annotations.NotNull;

/**
 * @author ysk
 * @class describe  {@link #}
 * @time 2019/4/22 下午4:29
 */
public class CommonRecyclerView extends BaseConstraintLayout<RecyclerCommonBinding> {
    public CommonRecyclerView(Context context) {
        super(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.recycler_common;
    }

    @NotNull
    @Override
    protected String getLayoutResIdName() {
        return "recycler_common";
    }

    @Override
    protected void initView() {

    }

    public void setData() {

    }
}
