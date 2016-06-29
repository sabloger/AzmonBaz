package com.kokabi.p.azmonbaz.Help;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kokabi.p.azmonbaz.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Constants {

    static SharedPreferences pref;

    /*Setting Parameters*/
    public static Boolean isShortcutCreated = false;

    //Config
    public final static String appFolder = "/AzmonBaz";
    public final static String catFolder = "/Categories";
    public final static String iconFolder = "/Icons";

    /*Fonts*/
    public final static String FONT_SANS = "fonts/sans.ttf";
    public final static String FONT_SANS_MEDIUM = "fonts/sans_medium.ttf";

    public static int[] questionList = {R.drawable.q1, R.drawable.q2, R.drawable.q3, R.drawable.q4, R.drawable.q5
            , R.drawable.q6, R.drawable.q7, R.drawable.q8, R.drawable.q9, R.drawable.q10};

    public static int[] answerList = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5
            , R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10};

    public static void loadPreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        Constants.isShortcutCreated = pref.getBoolean(GS.isShortcutCreated, Constants.isShortcutCreated);
    }

    public static void savePreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        pref.edit().putBoolean(GS.isShortcutCreated, Constants.isShortcutCreated).apply();
    }

    public static void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) AppController.getCurrentActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = AppController.getCurrentActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(AppController.getCurrentActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*SnackBar Actions*/
    public interface SNACK {
        int ERROR = 0;
        int WARNING = 1;
        int SUCCESS = 2;
    }

    public static String EncodeUTF(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String DecodeUTF(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String Decode(String str) {
        try {
//        MCrypt M = new MCrypt();
//        return M.decrypt(str);
        } catch (Exception e) {
        }
        return str;
    }

    public static String Encode(String str) {
        try {
//        MCrypt M = new MCrypt();
//        return M.encrypt(str);
        } catch (Exception e) {
        }
        return str;
    }

    public static boolean isOnline() {
        int NewStatus = NetworkUtil.getConnectivityStatus(AppController.getCurrentContext());
        return NewStatus != 0;
    }

}