package com.kokabi.p.azmonbaz.Components;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.FontChange;
import com.kokabi.p.azmonbaz.R;

/**
 * Created by P.kokabi on 1/17/2016.
 */
public class CustomSnackBar {

    String error;
    int type;
    View mainContent;
    static Typeface tf;

    public CustomSnackBar(View mainContent, String error, int type) {
        super();
        this.mainContent = mainContent;
        this.error = error;
        this.type = type;
        tf = FontChange.getTypeface(Constants.font.SANS_MEDIUM, AppController.getCurrentContext());
        Show();
    }

    public void Show() {
        switch (type) {
            case Constants.SNACK.ERROR: {
                Snackbar snackbar = Snackbar.make(mainContent, error, Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#E32C29"));
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTypeface(tf);
                textView.setTextColor(ContextCompat.getColor(AppController.getCurrentContext(), R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
            case Constants.SNACK.WARNING: {
                Snackbar snackbar = Snackbar.make(mainContent, error, Snackbar.LENGTH_LONG);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTypeface(tf);
                textView.setTextColor(ContextCompat.getColor(AppController.getCurrentContext(), R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
            case Constants.SNACK.SUCCESS: {
                Snackbar snackbar = Snackbar.make(mainContent, error, Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#2D9C42"));
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTypeface(tf);
                textView.setTextColor(ContextCompat.getColor(AppController.getCurrentContext(), R.color.white));
                textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                snackbar.show();
                break;
            }
        }
    }

}
