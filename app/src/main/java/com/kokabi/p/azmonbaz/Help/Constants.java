package com.kokabi.p.azmonbaz.Help;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kokabi.p.azmonbaz.Objects.CategoryObj;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Constants {

    static SharedPreferences pref;

    /*Setting Parameters*/
    public static boolean isShortcutCreated = false;
    public static boolean isDirectoriesCreated = false;

    //Config
    public final static String appFolder = "/AzmonBaz";
    public final static String catFolder = "/Categories";
    public final static String testDefinitionsFolder = "/TestDefinitions";
    public final static String testFolder = "/Test";
    public final static String iconFolder = "/Icons";
    public final static String questionsFolder = "/Questions";
    public final static String answersFolder = "/Answers";

    /*Fonts*/
    public final static String FONT_SANS = "fonts/sans.ttf";
    public final static String FONT_SANS_MEDIUM = "fonts/sans_medium.ttf";

    /*TreeValues*/
    public final static ArrayList<CategoryObj> totalCategories = new ArrayList<>();

    public static void loadPreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        Constants.isShortcutCreated = pref.getBoolean(GS.isShortcutCreated, Constants.isShortcutCreated);
        Constants.isDirectoriesCreated = pref.getBoolean(GS.isDirectoriesCreated, Constants.isDirectoriesCreated);
    }

    public static void savePreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        pref.edit().putBoolean(GS.isShortcutCreated, Constants.isShortcutCreated).apply();
        pref.edit().putBoolean(GS.isShortcutCreated, Constants.isDirectoriesCreated).apply();
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