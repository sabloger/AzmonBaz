package com.kokabi.p.azmonbaz.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kokabi.p.azmonbaz.R;

public class CSpinner extends LinearLayout {

    Context context;
    public CButton spinner;
    String text;

    public CSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public CSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.generalAttrs);
        text = typedArray.getString(R.styleable.generalAttrs_text);

        init();
        typedArray.recycle();

    }

    public void init() {
        if (isInEditMode())
            return;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.custom_spinner, null);

        spinner = (CButton) myView.findViewById(R.id.spinner);
        spinner.setText(text);

        addView(myView);
    }

    public void setText(String str) {
        spinner.setText(str);
    }

    public String getText() {
        return spinner.getText().toString();
    }
}