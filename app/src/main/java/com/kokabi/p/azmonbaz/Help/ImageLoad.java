package com.kokabi.p.azmonbaz.Help;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import com.kokabi.p.azmonbaz.Activities.CourseQuestionsActivity;

import java.io.IOException;
import java.io.InputStream;

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
        AssetManager assetManager = AppController.getCurrentContext().getAssets();
        try {
            InputStream is = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e(CourseQuestionsActivity.class.getName(), e.getMessage());
        }
    }
}
