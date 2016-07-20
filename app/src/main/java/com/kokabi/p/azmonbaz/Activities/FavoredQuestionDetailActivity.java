package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.ImageView;

import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.ImageLoad;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.kokabi on 7/20/2016.
 */
public class FavoredQuestionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    DataBase db;

    ImageView questionAnswer_imgv;
    AppCompatImageButton questionAnswer_imgbtn, close_imgbtn;

    /*Activity Values*/
    PhotoViewAttacher questionAnswerZoomable;
    int idQuestion;
    boolean isAnswer = false;
    TestObj testObj = new TestObj();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favored_question_detail);

        context = this;
        AppController.setActivityContext(FavoredQuestionDetailActivity.this, context);
        db = new DataBase(context);

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idQuestion = bundle.getInt("idQuestion", 0);
        }

        testObj = db.selectFavoredQuestion(idQuestion);

        new ImageLoad(testObj.getQuestionImage(), questionAnswer_imgv);

        questionAnswerZoomable = new PhotoViewAttacher(questionAnswer_imgv);
    }

    private void findViews() {
        questionAnswer_imgv = (ImageView) findViewById(R.id.questionAnswer_imgv);

        close_imgbtn = (AppCompatImageButton) findViewById(R.id.close_imgbtn);
        questionAnswer_imgbtn = (AppCompatImageButton) findViewById(R.id.questionAnswer_imgbtn);

        setOnClick();
    }

    private void setOnClick() {
        close_imgbtn.setOnClickListener(this);
        questionAnswer_imgbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_imgbtn:
                finish();
                break;
            case R.id.questionAnswer_imgbtn:
                if (!isAnswer) {
                    isAnswer = true;
                    questionAnswer_imgbtn.setImageResource(R.drawable.ic_question);
                    new ImageLoad(testObj.getAnswerImage(), questionAnswer_imgv);
                    questionAnswerZoomable.update();
                } else {
                    isAnswer = false;
                    questionAnswer_imgbtn.setImageResource(R.drawable.ic_question);
                    new ImageLoad(testObj.getQuestionImage(), questionAnswer_imgv);
                    questionAnswerZoomable.update();
                }
                break;
        }
    }
}
