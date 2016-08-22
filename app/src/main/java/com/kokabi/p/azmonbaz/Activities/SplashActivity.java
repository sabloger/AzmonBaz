package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;
import com.splunk.mint.Mint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    Context context;

    /*Activity Values*/
    int SPLASH_TIME_OUT = 500;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_splash);

        context = this;
        AppController.setActivityContext(SplashActivity.this, this);

        Constants.loadPreferences();
        welcomeScreen();
    }

    private void welcomeScreen() {
        /*Initial Crash Reporter*/
        Mint.initAndStartSession(SplashActivity.this, "b4a93f90");

        /*Create Shortcut on home screen*/
        if (!Constants.isShortcutCreated) {
            addShortcut();
        }

        if (!Constants.isDataLoaded) {
            Constants.totalCategories.addAll(categoryListMaker());
            Constants.totalTestTitles.addAll(testsTitleMaker());
            Constants.totalTestDef.addAll(testDefinitionMaker());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    /*Create ShortCut In Home Page*/
    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent installIntent = new Intent();
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "آزمون باز");
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
        shortcutIntent.addFlags(flags);
        installIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(installIntent);

        Constants.isShortcutCreated = true;
        Constants.savePreferences();

    }

    private ArrayList<CategoryObj> categoryListMaker() {
        ArrayList<CategoryObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource("categories.json")).getJSONArray("categories");
            for (int i = 0; i < categoryArray.length(); ++i) {
                result.add(new Gson().fromJson(categoryArray.getJSONObject(i).toString(), CategoryObj.class));
            }
        } catch (JSONException e) {
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }
        return result;
    }

    private ArrayList<TestsTitleObj> testsTitleMaker() {
        ArrayList<TestsTitleObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource("tests_title.json")).getJSONArray("test_titles");
            for (int i = 0; i < categoryArray.length(); ++i) {
                result.add(new Gson().fromJson(categoryArray.getJSONObject(i).toString(), TestsTitleObj.class));
            }
        } catch (JSONException e) {
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }
        return result;
    }

    private ArrayList<TestDefinitionObj> testDefinitionMaker() {
        ArrayList<TestDefinitionObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource("test_definition.json")).getJSONArray("testDefinition");
            for (int i = 0; i < categoryArray.length(); ++i) {
                result.add(new Gson().fromJson(categoryArray.getJSONObject(i).toString(), TestDefinitionObj.class));
            }
        } catch (JSONException e) {
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }
        return result;
    }
}
