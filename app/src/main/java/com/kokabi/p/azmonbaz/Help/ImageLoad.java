package com.kokabi.p.azmonbaz.Help;

import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
//            File fileEvents = new File(AppController.getCurrentContext().getApplicationInfo().dataDir
//                    + "/app_" + Constants.appFolderName + "/" + path);
//            Bitmap bitmap = BitmapFactory.decodeFile(Constants.decodeBase64(fileEvents.getAbsolutePath()));
            Bitmap bitmap = Constants.decodeBase64(readFile(path));
//            String s = Constants.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
            view.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i(Constants.TAG, e.getMessage());
        }
    }

    public String readFile(String filename) {
        File fileEvents = new File(AppController.getCurrentContext().getApplicationInfo().dataDir
                + "/app_" + Constants.appFolderName + "/" + filename);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}