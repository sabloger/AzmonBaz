package com.kokabi.p.azmonbaz.Components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

public class CEditText extends AppCompatEditText {
    Context ctx;

    public CEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
        init();
    }

    public CEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init();
    }

    public CEditText(Context context) {
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

        int right = (int) getResources().getDimension(R.dimen.editTextPaddingRight);
        int left = (int) getResources().getDimension(R.dimen.editTextPaddingLeft);
        int top = (int) getResources().getDimension(R.dimen.editTextPaddingTop);
        int bottom = (int) getResources().getDimension(R.dimen.editTextPaddingBottom);

        setPadding(left, top, right, bottom);
    }
}