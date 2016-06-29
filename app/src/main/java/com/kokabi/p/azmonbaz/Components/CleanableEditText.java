package com.kokabi.p.azmonbaz.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

public class CleanableEditText extends FrameLayout {

    Context context;
    public AppCompatEditText editText;
    ImageButton clear;
    String text, hint;
    boolean singleLine;
    int inputType, textColor, backgroundColor, hintColor, maxLength, gravity, imeOptions, textSize, tintColor;
    int maxLines, minLines, lines;

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public CleanableEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.generalAttrs);
        text = typedArray.getString(R.styleable.generalAttrs_text);
        textColor = typedArray.getColor(R.styleable.generalAttrs_textColor, 0x000000);
        backgroundColor = typedArray.getColor(R.styleable.generalAttrs_backgroundColor, 0x000000);
        hint = typedArray.getString(R.styleable.generalAttrs_hint);
        hintColor = typedArray.getColor(R.styleable.generalAttrs_hintColor, 0x000000);
        singleLine = typedArray.getBoolean(R.styleable.generalAttrs_singleLine, true);
        inputType = typedArray.getInt(R.styleable.generalAttrs_inputType, 0);
        maxLength = typedArray.getInt(R.styleable.generalAttrs_maxLength, 100);
        gravity = typedArray.getInt(R.styleable.generalAttrs_gravity, 0);
        imeOptions = typedArray.getInt(R.styleable.generalAttrs_imeOptions, 0);
        textSize = typedArray.getInt(R.styleable.generalAttrs_textSize, 14);
        tintColor = typedArray.getInt(R.styleable.generalAttrs_tintColor, 0x000000);
        maxLines = typedArray.getInt(R.styleable.generalAttrs_maxLines, 1);
        minLines = typedArray.getInt(R.styleable.generalAttrs_minLines, 1);
        lines = typedArray.getInt(R.styleable.generalAttrs_lines, 1);
        init();
        typedArray.recycle();

    }

    public void init() {
        if (isInEditMode())
            return;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.custom_cleanable_edit_text, null, false);

        editText = (AppCompatEditText) myView.findViewById(R.id.editText);

        clear = (ImageButton) myView.findViewById(R.id.clear);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Constants.FONT_SANS);
        editText.setTypeface(tf);

        clear.setVisibility(GONE);
        clear.setColorFilter(tintColor);
        editText.setText(text);
        editText.setTextColor(textColor);
        editText.setBackgroundColor(backgroundColor);
        editText.setHint(hint);
        editText.setHintTextColor(hintColor);
        editText.setSingleLine();
        editText.setGravity(gravity);
        editText.setImeOptions(imeOptions);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        editText.setMaxLines(maxLines);
        editText.setMinLines(minLines);
        editText.setLines(lines);
        /*set maxLength*/
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        /*========================================================================================*/
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().trim().length() > 0) {
                    clear.setVisibility(VISIBLE);
                } else {
                    clear.setVisibility(GONE);
                }
            }
        });

        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                clear.setVisibility(GONE);
            }
        });

        addView(myView);
    }

    public void setText(String str) {
        editText.setText(str);
    }

    public String getText() {
        return editText.getText().toString();
    }
}