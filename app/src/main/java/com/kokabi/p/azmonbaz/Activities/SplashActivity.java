package com.kokabi.p.azmonbaz.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.WindowManager;

import com.kokabi.p.azmonbaz.Components.CPermissionDeniedDialog;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

import java.io.File;


public class SplashActivity extends AppCompatActivity {

    Context context;
    CPermissionDeniedDialog dialogPermissionDenied;

    /*Activity Values*/
    private static final int REQUEST_CODE_PERMISSIONS = 1;
    String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(context, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, mPermission[2]) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(context, mPermission[3]) != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSIONS);
                } else {
                    welcomeScreen();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            welcomeScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length == 1 && grantResults[0] == MockPackageManager.PERMISSION_GRANTED) {
                welcomeScreen();

            } else {
                dialogPermissionDenied.show();
                dialogPermissionDenied.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        welcomeScreen();
                    }
                });
            }
        } else {
            welcomeScreen();
        }
    }

    private void welcomeScreen() {
        /*Initial Crash Reporter*/
//        Mint.initAndStartSession(SplashActivity.this, "1ec437c9");

/*        if (!Constants.isShortcutCreated) {
            AddShortcut();
        }*/

        /*Create Directory*/
        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + Constants.appFolder); //root directory

        File categories = new File(root.getAbsolutePath() + Constants.appFolder + Constants.catFolder);

        File icons = new File(root.getAbsolutePath() + Constants.appFolder + Constants.catFolder + Constants.iconFolder);

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!categories.exists()) {
                categories.mkdirs();
            }
            if (!icons.exists()) {
                icons.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        final int welcomeScreenDisplay = 1000;
        Thread welcomeThread = new Thread() {
            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();

                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    /*Create ShortCut In Home Page*/
    /*private void AddShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "تاریخ بان");
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
        shortcutIntent.addFlags(flags);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        Constants.isShortcutCreated = true;
        Constants.savePreferences();

    }*/
}
