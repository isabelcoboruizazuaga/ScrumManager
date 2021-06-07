package com.example.scrummanager.Controladores;

import android.content.Context;
import android.util.AttributeSet;

/**
 * ImageView modificador para ser proporcional al layout
 */
public class ProporcionalImageView extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * Constructor
     * @param context
     */
    public ProporcionalImageView(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public ProporcionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
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