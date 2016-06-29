package com.kokabi.p.azmonbaz.Components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.kokabi.p.azmonbaz.Help.Constants;

public class CButton extends Button {
    Context ctx;

    public CButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
        init();
    }

    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init();
    }

    public CButton(Context context) {
        super(context);
        ctx = context;
        init();
    }

    public void init() {
        if (isInEditMode())
            return;
        setOnTouchListener(null);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_SANS);
        setTypeface(tf);
    }
}