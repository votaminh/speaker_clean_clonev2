package com.msc.demo_mvvm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Relative1Per1 extends RelativeLayout {
    public Relative1Per1(@NonNull Context context) {
        super(context);
    }

    public Relative1Per1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Relative1Per1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
