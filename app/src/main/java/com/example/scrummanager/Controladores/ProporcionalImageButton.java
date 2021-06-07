package com.example.scrummanager.Controladores;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * ImageButton modificado para ser proporcional al layout
 */
public class ProporcionalImageButton extends androidx.appcompat.widget.AppCompatImageButton {

    /**
     * Constructor
     * @param context
     */
    public ProporcionalImageButton(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public ProporcionalImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProporcionalImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }
}