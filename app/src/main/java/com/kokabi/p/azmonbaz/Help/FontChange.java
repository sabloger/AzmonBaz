package com.kokabi.p.azmonbaz.Help;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Payam on 5/28/15.
 */
public class FontChange {
    public FontChange() {
    }

    public static void setTextViewFont(Typeface tf, TextView... params) {
        for (TextView tv : params) {
            tv.setTypeface(tf);
        }
    }

}
