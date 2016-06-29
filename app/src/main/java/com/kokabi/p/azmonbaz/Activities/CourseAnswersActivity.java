package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.Kokabi on 6/23/16.
 */
public class CourseAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    CoordinatorLayout mainContent;
    TextView title_tv, correctAnswer_tv, userAnswer_tv;
    AppCompatImageButton close_imgbtn;
    LinearLayout nextQuestion_ly, previousQuestion_ly;
    ImageView answer_imgv;

    /*Activity Values*/
    PhotoViewAttacher answerZoomable;
    int answer = 0, totalAnswer = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_answers);

        context = this;
        AppController.setActivityContext(CourseAnswersActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));

        answer_imgv.setImageResource(Constants.answerList[answer]);
        answerZoomable = new PhotoViewAttacher(answer_imgv);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_imgbtn:
                finish();
                break;
            case R.id.nextQuestion_ly:
                if (answer < totalAnswer) {
                    answer++;
                    answer_imgv.setImageResource(Constants.answerList[answer]);
                    answerZoomable.update();
                    title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
                }
                break;
            case R.id.previousQuestion_ly:
                if (answer > 0) {
                    answer--;
                    answer_imgv.setImageResource(Constants.answerList[answer]);
                    answerZoomable.update();
                    title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
                }
                break;
        }
    }

    private void findViews() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        correctAnswer_tv = (TextView) findViewById(R.id.correctAnswer_tv);
        userAnswer_tv = (TextView) findViewById(R.id.userAnswer_tv);

        close_imgbtn = (AppCompatImageButton) findViewById(R.id.close_imgbtn);

        nextQuestion_ly = (LinearLayout) findViewById(R.id.nextQuestion_ly);
        previousQuestion_ly = (LinearLayout) findViewById(R.id.previousQuestion_ly);

        answer_imgv = (ImageView) findViewById(R.id.answer_imgv);

        setOnClick();
    }

    private void setOnClick() {
        close_imgbtn.setOnClickListener(this);
        nextQuestion_ly.setOnClickListener(this);
        previousQuestion_ly.setOnClickListener(this);
    }

}
