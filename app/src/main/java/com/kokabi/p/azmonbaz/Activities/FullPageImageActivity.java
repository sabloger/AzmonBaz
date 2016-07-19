package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kokabi.p.azmonbaz.R;

import java.io.IOException;
import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.kokabi on 7/19/2016.
 */
public class FullPageImageActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    ImageView question_imgv;
    AppCompatImageButton exit_full_imgbtn;

    /*Activity Values*/
    PhotoViewAttacher questionZoomable;
    String srcImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page_image);

        context = this;

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            srcImage = bundle.getString("srcImage", "");
        }

        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open(srcImage);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            question_imgv.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e(CourseQuestionsActivity.class.getName(), e.getMessage());
        }

        questionZoomable = new PhotoViewAttacher(question_imgv);

    }

    private void findViews() {
        question_imgv = (ImageView) findViewById(R.id.question_imgv);

        exit_full_imgbtn = (AppCompatImageButton) findViewById(R.id.exit_full_imgbtn);

        setOnClick();
    }

    private void setOnClick() {
        exit_full_imgbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_full_imgbtn:
                finish();
                break;
        }
    }
}
