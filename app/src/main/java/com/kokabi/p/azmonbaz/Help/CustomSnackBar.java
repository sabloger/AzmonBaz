package com.kokabi.p.azmonbaz.Help;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.R;

/**
 * Created by P.kokabi on 1/17/2016.
 */
public class CustomSnackBar {

    String Error;
    int Type;
    Context context;
    View mainContent;
    static Typeface Tf;

    public CustomSnackBar(Context context, View mainContent, String error, int type) {
        super();
        this.context = context;
        this.mainContent = mainContent;
        Error = error;
        Type = type;
        Show();
    }

    public void Show() {
        switch (Type) {
            case Constants.SNACK.ERROR: {
                Snackbar snackbar = Snackbar.make(mainContent, Error, Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#E32C29"));
                Tf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_SANS_MEDIUM);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                FontChange.setTextViewFont(Tf, textView);
                textView.setTextColor(context.getResources().getColor(R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
            case Constants.SNACK.WARNING: {
                Snackbar snackbar = Snackbar.make(mainContent, Error, Snackbar.LENGTH_LONG);
                Tf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_SANS_MEDIUM);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                FontChange.setTextViewFont(Tf, textView);
                textView.setTextColor(context.getResources().getColor(R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
            case Constants.SNACK.SUCCESS: {
                Snackbar snackbar = Snackbar.make(mainContent, Error, Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#2D9C42"));
                Tf = Typeface.createFromAsset(context.getAssets(), Constants.FONT_SANS_MEDIUM);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                FontChange.setTextViewFont(Tf, textView);
                textView.setTextColor(context.getResources().getColor(R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
        }
    }

}
