package com.kokabi.p.azmonbaz.Components;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.FontChange;

public class CTextViewSansMedium extends AppCompatTextView {
    Context ctx;

    public CTextViewSansMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
        init();
    }

    public CTextViewSansMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init();
    }

    public CTextViewSansMedium(Context context) {
        super(context);
        ctx = context;
        init();
    }

    public void init() {
        if (isInEditMode())
            return;
        setOnTouchListener(null);
        FontChange.setFontTextView(this, Constants.font.SANS_MEDIUM, ctx);
    }
}