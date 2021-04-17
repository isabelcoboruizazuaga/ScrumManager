package com.example.scrummanager.Controladores;

import android.content.Context;
import android.util.AttributeSet;

public class ProporcionalImageView extends androidx.appcompat.widget.AppCompatImageView {

    public ProporcionalImageView(Context context) {
        super(context);
    }

    public ProporcionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProporcionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }
}