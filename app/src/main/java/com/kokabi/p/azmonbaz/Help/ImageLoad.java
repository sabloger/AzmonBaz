package com.kokabi.p.azmonbaz.Help;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import java.io.File;

/**
 * Created by P.kokabi on 7/20/2016.
 */
public class ImageLoad {

    String path;
    AppCompatImageView view;

    public ImageLoad(String path, AppCompatImageView view) {
        this.path = path;
        this.view = view;
        loadImage();
    }

    private void loadImage() {
        try {
            File fileEvents = new File(AppController.getCurrentContext().getApplicationInfo().dataDir
                    + "/app_" + Constants.appFolderName + "/" + path);
            Bitmap bitmap = BitmapFactory.decodeFile(fileEvents.getAbsolutePath());
            view.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i(Constants.TAG, e.getMessage());
        }
    }
}
