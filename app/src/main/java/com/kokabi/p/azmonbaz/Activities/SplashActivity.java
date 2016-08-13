package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.kokabi.p.azmonbaz.Components.CPermissionDeniedDialog;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;
import com.splunk.mint.Mint;


public class SplashActivity extends AppCompatActivity {

    Context context;
    CPermissionDeniedDialog dialogPermissionDenied;

    /*Activity Values*/
    int SPLASH_TIME_OUT = 1000;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_splash);

        context = this;
        AppController.setActivityContext(SplashActivity.this, this);
        dialogPermissionDenied = new CPermissionDeniedDialog();

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
}
