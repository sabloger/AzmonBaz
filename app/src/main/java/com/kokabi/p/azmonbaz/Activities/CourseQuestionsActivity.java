package com.kokabi.p.azmonbaz.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.R;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.Kokabi on 6/23/16.
 */
public class CourseQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Dialog dialogResults;

    CoordinatorLayout mainContent;
    TextView title_tv, timer_tv, numberOfQuestions_tv;
    AppCompatImageButton close_imgbtn;
    ProgressView progressBar;
    LinearLayout nextQuestion_ly, previousQuestion_ly;
    ImageView question_imgv;
    Button firstChoice_btn, secondChoice_btn, thirdChoice_btn, fourthChoice_btn;
    FloatingActionButton confirm_fab, numberOfQuestions_fab;

    /*Activity Values*/
    PhotoViewAttacher questionZoomable;
    CountDownTimer countDownTimer;
    int question = 0, totalQuestion = 9, whichAnswer = 0;
    ArrayList<Integer> answerList = new ArrayList<>();
    ArrayList<Integer> answerKeyList = new ArrayList<>();
    ArrayList<Integer> correctAnsweredList = new ArrayList<>();
    ArrayList<Integer> unAnsweredList = new ArrayList<>();
    ArrayList<Integer> inCorrectAnsweredList = new ArrayList<>();
    String title = "آزمون ریاضی دیفرانسیل";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_questions);

        context = this;
        AppController.setActivityContext(CourseQuestionsActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        /*Creating DialogResults==================================================================*/
        dialogResults = new Dialog(this);
        dialogResults.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogResults.setContentView(R.layout.dialog_test_results);
        /*========================================================================================*/

        findViews();
        timer(600000);
        addCorrectAnswer();

        title_tv.setText(title);

        progressBar.setProgress(0f);
        progressBar.start();

        question_imgv.setImageResource(Constants.questionList[0]);
        questionZoomable = new PhotoViewAttacher(question_imgv);

        numberOfQuestions_tv.setText(String.valueOf((question + 1) + "/" + (totalQuestion + 1)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_imgbtn:
                finish();
                break;
            case R.id.nextQuestion_ly:
                if (question < totalQuestion) {
                    addAnswer();
                    question++;
                    updatePage();
                    if (question == totalQuestion) {
                        confirm_fab.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.previousQuestion_ly:
                if (question > 0) {
                    addAnswer();
                    question--;
                    updatePage();
                    confirm_fab.setVisibility(View.GONE);
                }
                break;
            case R.id.firstChoice_btn:
                if (whichAnswer == 1) {
                    whichAnswer = 0;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                } else {
                    whichAnswer = 1;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                }
                secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                break;
            case R.id.secondChoice_btn:
                if (whichAnswer == 2) {
                    whichAnswer = 0;
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                } else {
                    whichAnswer = 2;
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                }
                firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                break;
            case R.id.thirdChoice_btn:
                if (whichAnswer == 3) {
                    whichAnswer = 0;
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                } else {
                    whichAnswer = 3;
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                }
                firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                break;
            case R.id.fourthChoice_btn:
                if (whichAnswer == 4) {
                    whichAnswer = 0;
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                } else {
                    whichAnswer = 4;
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                }
                firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                break;
            case R.id.confirm_fab:
                addAnswer();
                compareAnswers();
                showDialogResults();
                break;
        }
    }

    private void findViews() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        timer_tv = (TextView) findViewById(R.id.timer_tv);
        numberOfQuestions_tv = (TextView) findViewById(R.id.numberOfQuestions_tv);

        close_imgbtn = (AppCompatImageButton) findViewById(R.id.close_imgbtn);

        progressBar = (ProgressView) findViewById(R.id.progressBar);

        nextQuestion_ly = (LinearLayout) findViewById(R.id.nextQuestion_ly);
        previousQuestion_ly = (LinearLayout) findViewById(R.id.previousQuestion_ly);

        question_imgv = (ImageView) findViewById(R.id.question_imgv);

        firstChoice_btn = (Button) findViewById(R.id.firstChoice_btn);
        secondChoice_btn = (Button) findViewById(R.id.secondChoice_btn);
        thirdChoice_btn = (Button) findViewById(R.id.thirdChoice_btn);
        fourthChoice_btn = (Button) findViewById(R.id.fourthChoice_btn);

        confirm_fab = (FloatingActionButton) findViewById(R.id.confirm_fab);
        numberOfQuestions_fab = (FloatingActionButton) findViewById(R.id.numberOfQuestions_fab);

        setOnClick();
    }

    private void setOnClick() {
        close_imgbtn.setOnClickListener(this);
        nextQuestion_ly.setOnClickListener(this);
        previousQuestion_ly.setOnClickListener(this);
        firstChoice_btn.setOnClickListener(this);
        secondChoice_btn.setOnClickListener(this);
        thirdChoice_btn.setOnClickListener(this);
        fourthChoice_btn.setOnClickListener(this);
        confirm_fab.setOnClickListener(this);
        confirm_fab.setOnClickListener(this);
    }

    private void timer(long milliSeconds) {
        countDownTimer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressBar.getProgress() + 0.001666f);
                timer_tv.setText(String.valueOf(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
            }

            public void onFinish() {
                timer_tv.setText("0 : 0");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1500);
            }
        }.start();
    }

    private void updatePage() {
        numberOfQuestions_tv.setText(String.valueOf((question + 1) + "/" + (totalQuestion + 1)));
        question_imgv.setImageResource(Constants.questionList[question]);
        questionZoomable.update();
        Log.i("Answer", answerList.toString());
        if (answerList.size() >= (question + 1)) {
            switch (answerList.get(question)) {
                case 1:
                    whichAnswer = 1;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    break;
                case 2:
                    whichAnswer = 2;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    break;
                case 3:
                    whichAnswer = 3;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    break;
                case 4:
                    whichAnswer = 4;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_light);
                    break;
                default:
                    whichAnswer = 0;
                    firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
                    break;
            }
        } else {
            whichAnswer = 0;
            firstChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
            secondChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
            thirdChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
            fourthChoice_btn.setBackgroundResource(R.drawable.radius_btn_dark);
        }
    }

    private void addAnswer() {
        if (answerList.size() > 0) {
            if (answerList.size() < (question + 1)) {
                answerList.add(whichAnswer);
            } else {
                answerList.set(question, whichAnswer);
            }
        } else {
            if (whichAnswer != 0) {
                answerList.add(whichAnswer);
            } else {
                answerList.add(0);
            }
        }
        Log.i("question", question + "");
        Log.i("Answer1", answerList.toString());
    }

    private void addCorrectAnswer() {
        answerKeyList.add(4);
        answerKeyList.add(2);
        answerKeyList.add(2);
        answerKeyList.add(1);
        answerKeyList.add(1);
        answerKeyList.add(3);
        answerKeyList.add(3);
        answerKeyList.add(4);
        answerKeyList.add(4);
        answerKeyList.add(4);
    }

    private void compareAnswers() {
        for (int i = 0; i < answerKeyList.size(); i++) {
            if (answerKeyList.get(i).equals(answerList.get(i))) {
                correctAnsweredList.add(answerKeyList.get(i));
            } else if (answerList.get(i).equals(0)) {
                unAnsweredList.add(0);
            } else {
                inCorrectAnsweredList.add(i);
            }
        }
    }

    private void showDialogResults() {
        TextView title_tv = (TextView) dialogResults.findViewById(R.id.title_tv);
        TextView sub_title_tv = (TextView) dialogResults.findViewById(R.id.sub_title_tv);
        TextView results_tv = (TextView) dialogResults.findViewById(R.id.results_tv);
        TextView seeResults_btn = (Button) dialogResults.findViewById(R.id.seeResults_btn);

        title_tv.setText(String.valueOf("نتایج " + title));
        sub_title_tv.setText(String.valueOf("شما به " + (correctAnsweredList.size() * 10) + "٪ از سوالات پاسخ داده اید "));
        results_tv.setText(String.valueOf(correctAnsweredList.size() + " پاسخ صحیح " + "\n" + unAnsweredList.size()
                + " سوال جواب نداده" + "\n" + inCorrectAnsweredList.size() + " پاسخ اشتباه"));

        seeResults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResults.dismiss();
                finish();
                startActivity(new Intent(context, CourseAnswersActivity.class));
            }
        });

        dialogResults.show();
    }

}
