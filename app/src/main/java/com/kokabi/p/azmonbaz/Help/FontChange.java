package com.kokabi.p.azmonbaz.Help;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import java.util.Hashtable;

/**
 * Created by R.Miri on 7/20/2016.
 */
public class FontChange {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();


    public static void setFontTextView(TextView view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontEditText(EditText view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontButton(Button view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontRadioButton(RadioButton view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontCheckBox(CheckBox view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontTextInputLayout(TextInputLayout view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static void setFontSwitch(Switch view, String font, Context context) {
        Typeface tf = getTypeface(font, context);
        if (tf != null) {
            view.setTypeface(tf);
        }
    }

    public static Typeface getTypeface(String font, Context context) {
        Typeface tf = fontCache.get(font);
        if (tf == null) {
            try {
                if (font.equals(Constants.font.SANS)) {
                    tf = Typeface.createFromAsset(context.getAssets(), Constants.font.SANS);
                } else if (font.equals(Constants.font.SANS_MEDIUM)) {
                    tf = Typeface.createFromAsset(context.getAssets(), Constants.font.SANS_MEDIUM);
                }
            } catch (Exception e) {
                return null;
            }
            fontCache.put(font, tf);
        }
        return tf;
    }
}
