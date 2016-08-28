package com.kokabi.p.azmonbaz.Help;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.Objects.GeneralObj;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

    static SharedPreferences pref;
    public static String TAG = "AzmonBaz";

    /*Setting Parameters*/
    public static boolean isShortcutCreated = false;
    public static boolean isDataLoaded = false;

    /*Fonts*/
    public interface font {
        String SANS = "fonts/sans.ttf";
        String SANS_MEDIUM = "fonts/sans_medium.ttf";
    }

    /*TreeValues*/
    public final static ArrayList<CategoryObj> totalCategories = new ArrayList<>();
    public final static ArrayList<TestsTitleObj> totalTestTitles = new ArrayList<>();
    public final static ArrayList<TestDefinitionObj> totalTestDef = new ArrayList<>();
    public static String totalCategoriesAsString = "";
    public static String totalTestTitlesAsString = "";
    public static String totalTestDefAsString = "";

    public static void loadPreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        Constants.isShortcutCreated = pref.getBoolean(GS.isShortcutCreated, Constants.isShortcutCreated);
        Constants.isDataLoaded = pref.getBoolean(GS.isDataLoaded, Constants.isDataLoaded);
        Constants.totalCategoriesAsString = pref.getString(GS.totalCategoriesAsString, Constants.totalCategoriesAsString);
        Constants.totalTestTitlesAsString = pref.getString(GS.totalTestTitlesAsString, Constants.totalTestTitlesAsString);
        Constants.totalTestDefAsString = pref.getString(GS.totalTestDefAsString, Constants.totalTestDefAsString);
    }

    public static void savePreferences() {
        pref = AppController.getCurrentContext().getSharedPreferences("i", Context.MODE_PRIVATE);

        pref.edit().putBoolean(GS.isShortcutCreated, Constants.isShortcutCreated).apply();
        pref.edit().putBoolean(GS.isDataLoaded, Constants.isDataLoaded).apply();
        pref.edit().putString(GS.totalCategoriesAsString, Constants.totalCategoriesAsString).apply();
        pref.edit().putString(GS.totalTestTitlesAsString, Constants.totalTestTitlesAsString).apply();
        pref.edit().putString(GS.totalTestDefAsString, Constants.totalTestDefAsString).apply();
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static boolean containsKey(HashMap<Integer, Integer> hashMap, int key) {
        return hashMap.get(key) != null;
    }

    public static GeneralObj getChildSize(int id) {
        int size = 0, done = 0;
        for (int i = 0; i < totalTestTitles.size(); i++) {
            if (totalTestTitles.get(i).getIdCat() == id) {
                size++;
                if (new DataBase().isTestAnswered(totalTestTitles.get(i).getIdTest())) {
                    done++;
                }
            }
        }

        if (size == 0) {
            for (int j = 0; j < totalCategories.size(); j++) {
                if (totalCategories.get(j).getIdParent() == id) {
                    for (int k = 0; k < totalTestTitles.size(); k++) {
                        if (totalTestTitles.get(k).getIdCat() == totalCategories.get(j).getIdCat()) {
                            size++;
                            if (new DataBase().isTestAnswered(totalTestTitles.get(k).getIdTest())) {
                                done++;
                            }
                        }
                    }
                }
            }
        }

        return new GeneralObj(done, size);
    }

    /*SnackBar Actions*/
    public interface SNACK {
        int ERROR = 0;
        int WARNING = 1;
        int SUCCESS = 2;
    }

    public static boolean isOnline() {
        int NewStatus = NetworkUtil.getConnectivityStatus(AppController.getCurrentContext());
        return NewStatus != 0;
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

}