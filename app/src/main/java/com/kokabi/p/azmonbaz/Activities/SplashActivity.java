package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;
import com.splunk.mint.Mint;

import net.lingala.zip4j.core.ZipFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


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
        Constants.totalCategories.clear();
        Constants.totalTestTitles.clear();
        Constants.totalTestDef.clear();

        welcomeScreen();
    }

    private void welcomeScreen() {
        /*Initial Crash Reporter*/
        Mint.initAndStartSession(SplashActivity.this, "b4a93f90");

        /*Create Shortcut on home screen*/
        if (!Constants.isShortcutCreated) {
            addShortcut();
        }

        /*Unzip Files*/
        if (!Constants.isUnzipped) {
            unzipFile();
        }

        /*Load Data*/
        Gson gson = new Gson();
        if (!Constants.isDataLoaded) {
            Constants.totalCategories.addAll(categoryListMaker());
            Constants.totalTestTitles.addAll(testsTitleMaker());
            Constants.totalTestDef.addAll(testDefinitionMaker());
            Constants.totalCategoriesAsString = gson.toJson(Constants.totalCategories);
            Constants.totalTestTitlesAsString = gson.toJson(Constants.totalTestTitles);
            Constants.totalTestDefAsString = gson.toJson(Constants.totalTestDef);
            Constants.freeMemory();
            Constants.isDataLoaded = true;
            Constants.savePreferences();
        } else {
            Constants.totalCategories.addAll(Arrays.asList(gson.fromJson(Constants.totalCategoriesAsString, CategoryObj[].class)));
            Constants.totalTestTitles.addAll(Arrays.asList(gson.fromJson(Constants.totalTestTitlesAsString, TestsTitleObj[].class)));
            Constants.totalTestDef.addAll(Arrays.asList(gson.fromJson(Constants.totalTestDefAsString, TestDefinitionObj[].class)));
            Constants.freeMemory();
        }

        if (Constants.isUnzipped) {
            loadApplication();
        }

    }

    private void loadApplication() {
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

    /*Copy and Unzip Content Folder to Phone*/
    private void unzipFile() {
        if (copyFile(Constants.fileNameZip)) {
            String source = getApplicationContext().getApplicationInfo().dataDir + "/app_" + Constants.appFolderName + "/" + Constants.fileNameZip;
            String destination = getApplicationContext().getApplicationInfo().dataDir + "/app_" + Constants.appFolderName + "/";
            try {
                ZipFile zipFile = new ZipFile(source);
                if (zipFile.isEncrypted()) {
                    zipFile.setPassword(Constants.password);
                }
                zipFile.extractAll(destination);
                Constants.isUnzipped = true;
            } catch (net.lingala.zip4j.exception.ZipException e) {
                new DialogGeneral(context.getResources().getString(R.string.unzipFailure)
                        , getString(R.string.tryAgain), null, false) {
                    @Override
                    public void onConfirm() {
                        unzipFile();
                        if (Constants.isUnzipped) {
                            loadApplication();
                        }
                    }
                }.show();
                Constants.isUnzipped = false;
                Log.i(Constants.TAG, e.getMessage());
            }
        } else {
            new DialogGeneral(context.getResources().getString(R.string.unzipFailure)
                    , getString(R.string.tryAgain), null, false) {
                @Override
                public void onConfirm() {
                    unzipFile();
                    if (Constants.isUnzipped) {
                        loadApplication();
                    }
                }
            }.show();
            Constants.isUnzipped = false;
        }
        Constants.savePreferences();
    }

    private boolean copyFile(String filename) {
        File newDir = getApplicationContext().getDir(Constants.appFolderName, Context.MODE_PRIVATE);
        if (!newDir.exists()) {
            Constants.isUnzipped = newDir.mkdirs();
            Constants.savePreferences();
        }
        try {
            InputStream in = this.getAssets().open(filename);
            String newFileName = "/data/data/" + this.getPackageName() + "/app_" + Constants.appFolderName + "/" + filename;
            OutputStream out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
            return false;
        }
    }

    /*Load  Data*/
    private ArrayList<CategoryObj> categoryListMaker() {
        ArrayList<CategoryObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource("categories.json")).getJSONArray("categories");
            for (int i = 0; i < categoryArray.length(); ++i) {
                result.add(new Gson().fromJson(categoryArray.getJSONObject(i).toString(), CategoryObj.class));
            }
        } catch (JSONException e) {
            Constants.isDataLoaded = false;
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
            Constants.isDataLoaded = false;
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
            Constants.isDataLoaded = false;
            Log.e(SplashActivity.class.getName(), e.getMessage());
        }
        return result;
    }
}
