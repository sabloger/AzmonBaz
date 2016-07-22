package com.kokabi.p.azmonbaz.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.CustomSnackBar;
import com.kokabi.p.azmonbaz.Help.ImageLoad;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Help.BlurBuilder;
import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;
import com.rey.material.widget.ProgressView;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.Kokabi on 6/23/16.
 */
public class CourseQuestionsActivity extends AppCompatActivity implements DroppyClickCallbackInterface, View.OnClickListener {

    Context context;
    DataBase db;
    Dialog dialogResults;
    Dialog dialogSaveTest;
    CustomSnackBar snackBar;

    CoordinatorLayout mainContent;
    TextView timer_tv, numberOfQuestions_tv;
    AppCompatImageButton more_imgbtn, close_imgbtn, pausePlay_imgbtn, full_imgbtn;
    ProgressView progressBar;
    LinearLayout rootView, nextQuestion_ly, previousQuestion_ly;
    AppCompatImageButton addToFavoredQuestion_imgbtn, minus_imgbtn, cross_imgbtn;
    ImageView question_imgv, pauseLayout;
    Button firstChoice_btn, secondChoice_btn, thirdChoice_btn, fourthChoice_btn;
    FloatingActionButton confirm_fab;

    /*Activity Values*/
    PhotoViewAttacher questionZoomable;
    CountDownTimer countDownTimer;
    boolean hasNegativePoint = false, isPaused = false, isCanceled = false, isMinus = false, isCross = false, isAnswered = false;
    int idTest = 0, time = 0, question = 0, totalQuestion = 0, whichAnswer = 0, questionState = 3;
    long timeRemaining = 0;
    String testName = "";
    TestDefinitionObj pageTest;
    ArrayList<Integer> answerList = new ArrayList<>();
    ArrayList<Integer> correctAnsweredList = new ArrayList<>();
    ArrayList<Integer> unAnsweredList = new ArrayList<>();
    ArrayList<Integer> inCorrectAnsweredList = new ArrayList<>();
    //Declare a variable to hold CountDownTimer remaining time
    String decimal = "%02d : %02d";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_questions);

        context = this;
        db = new DataBase(context);
        AppController.setActivityContext(CourseQuestionsActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        /*Creating DialogResults==================================================================*/
        dialogResults = new Dialog(this);
        dialogResults.setCancelable(false);
        dialogResults.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogResults.setContentView(R.layout.dialog_test_results);
        /*Creating DialogSaveTest=================================================================*/
        dialogSaveTest = new Dialog(this);
        dialogSaveTest.setCancelable(false);
        dialogSaveTest.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSaveTest.setContentView(R.layout.dialog_save_test);
        /*========================================================================================*/

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idTest = bundle.getInt("idTest", 0);
            time = bundle.getInt("time", 0);
            testName = bundle.getString("testName", "");
            hasNegativePoint = bundle.getBoolean("hasNegativePoint", false);
        }

        for (int i = 0; i < pageMaker().size(); i++) {
            if (pageMaker().get(i).getIdTest() == idTest) {
                pageTest = new TestDefinitionObj(pageMaker().get(i).getIdTest(), pageMaker().get(i).getQuestionNo()
                        , pageMaker().get(i).getQuestionInfo(), pageMaker().get(i).getPercentage(), pageMaker().get(i).getLevel());
            }
        }

        timer(time * 1000);

        progressBar.setProgress(0f);
        progressBar.start();

        totalQuestion = pageTest.getQuestionNo() - 1;
        hideShowBackForward(question + 1);

        showQuestions(0);
        questionZoomable = new PhotoViewAttacher(question_imgv);

        numberOfQuestions_tv.setText(String.valueOf((question + 1) + " / " + (totalQuestion + 1)));

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        countDownTimer.cancel();
        pausePlay_imgbtn.setImageResource(R.drawable.ic_play);
        blurPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            isPaused = false;
            timer(timeRemaining);
            pausePlay_imgbtn.setImageResource(R.drawable.ic_pause);
            blurPage();
        }
    }

    @Override
    public void onBackPressed() {
        showDialogSaveTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void call(View v, int id) {
        switch (id) {
            case R.id.finishTest:
                if (answerList.size() > 0) {
                    addAnswer();
                } else {
                    for (int i = 0; i < pageTest.getQuestionInfo().size(); i++) {
                        answerList.add(0);
                    }
                }
                compareAnswers();
                showDialogResults();
                break;
            case R.id.addToFavorite:
                if (db.isTestFavored(idTest)) {
                    snackBar = new CustomSnackBar(mainContent, "این آزمون قبلا به آزمون های منتخب اضافه شده", Constants.SNACK.ERROR);
                } else {
                    db.favoriteTestInsert(new TestsTitleObj(idTest));
                    snackBar = new CustomSnackBar(mainContent, "این آزمون به آزمون‌های منتخب شما اضافه شد", Constants.SNACK.SUCCESS);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_imgbtn:
                showDialogSaveTest();
                break;
            case R.id.more_imgbtn:
                initMenu(more_imgbtn);
                break;
            case R.id.pausePlay_imgbtn:
                if (isPaused) {
                    isPaused = false;
                    blurPage();
                    timer(timeRemaining);
                    pausePlay_imgbtn.setImageResource(R.drawable.ic_pause);
                } else {
                    isPaused = true;
                    blurPage();
                    pausePlay_imgbtn.setImageResource(R.drawable.ic_play);
                    countDownTimer.cancel();
                }
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
            case R.id.full_imgbtn:
                startActivity(new Intent(context, FullPageImageActivity.class)
                        .putExtra("srcImage", "TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionInfo().get(question).getQuestionImage())
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;
            case R.id.firstChoice_btn:
                if (whichAnswer == 1) {
                    whichAnswer = 0;
                } else {
                    whichAnswer = 1;
                }
                selectButton(whichAnswer);
                break;
            case R.id.secondChoice_btn:
                if (whichAnswer == 2) {
                    whichAnswer = 0;
                } else {
                    whichAnswer = 2;
                }
                selectButton(whichAnswer);
                break;
            case R.id.thirdChoice_btn:
                if (whichAnswer == 3) {
                    whichAnswer = 0;
                } else {
                    whichAnswer = 3;
                }
                selectButton(whichAnswer);
                break;
            case R.id.fourthChoice_btn:
                if (whichAnswer == 4) {
                    whichAnswer = 0;
                } else {
                    whichAnswer = 4;
                }
                selectButton(whichAnswer);
                break;
            case R.id.addToFavoredQuestion_imgbtn:
                if (db.isQuestionFavored(pageTest.getQuestionInfo().get(question).getIdQuestion())) {
                    addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark_plus_outline);
                    db.favoredQuestionDelete(pageTest.getQuestionInfo().get(question).getIdQuestion());
                } else {
                    db.favoredQuestionInsert(new TestObj(testName, pageTest.getQuestionInfo().get(question).getIdQuestion(),
                            String.valueOf("TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionInfo().get(question).getQuestionImage()),
                            String.valueOf("TestDefinitions/" + pageTest.getIdTest() + "/a/" + pageTest.getQuestionInfo().get(question).getAnswerImage()),
                            pageTest.getQuestionInfo().get(question).getKey()));
                    addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark);
                }
                break;
            case R.id.minus_imgbtn:
                if (isMinus) {
                    minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                    isMinus = false;
                } else {
                    minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.green));
                    cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                    isMinus = true;
                    isCross = false;
                }
                addQuestionState();
                break;
            case R.id.cross_imgbtn:
                if (isCross) {
                    cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                    isCross = false;
                } else {
                    cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.accentColor));
                    minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                    isCross = true;
                    isMinus = false;
                }
                addQuestionState();
                break;
            case R.id.confirm_fab:
                addAnswer();
                compareAnswers();
                showDialogResults();
                break;
        }
    }

    private void findViews() {
        timer_tv = (TextView) findViewById(R.id.timer_tv);
        numberOfQuestions_tv = (TextView) findViewById(R.id.numberOfQuestions_tv);

        more_imgbtn = (AppCompatImageButton) findViewById(R.id.more_imgbtn);
        close_imgbtn = (AppCompatImageButton) findViewById(R.id.close_imgbtn);
        pausePlay_imgbtn = (AppCompatImageButton) findViewById(R.id.pausePlay_imgbtn);
        full_imgbtn = (AppCompatImageButton) findViewById(R.id.full_imgbtn);

        progressBar = (ProgressView) findViewById(R.id.progressBar);

        rootView = (LinearLayout) findViewById(R.id.rootView);
        nextQuestion_ly = (LinearLayout) findViewById(R.id.nextQuestion_ly);
        previousQuestion_ly = (LinearLayout) findViewById(R.id.previousQuestion_ly);

        addToFavoredQuestion_imgbtn = (AppCompatImageButton) findViewById(R.id.addToFavoredQuestion_imgbtn);
        minus_imgbtn = (AppCompatImageButton) findViewById(R.id.minus_imgbtn);
        cross_imgbtn = (AppCompatImageButton) findViewById(R.id.cross_imgbtn);

        question_imgv = (ImageView) findViewById(R.id.question_imgv);
        pauseLayout = (ImageView) findViewById(R.id.pauseLayout);

        firstChoice_btn = (Button) findViewById(R.id.firstChoice_btn);
        secondChoice_btn = (Button) findViewById(R.id.secondChoice_btn);
        thirdChoice_btn = (Button) findViewById(R.id.thirdChoice_btn);
        fourthChoice_btn = (Button) findViewById(R.id.fourthChoice_btn);

        confirm_fab = (FloatingActionButton) findViewById(R.id.confirm_fab);

        setOnClick();
    }

    private void setOnClick() {
        more_imgbtn.setOnClickListener(this);
        close_imgbtn.setOnClickListener(this);
        pausePlay_imgbtn.setOnClickListener(this);
        nextQuestion_ly.setOnClickListener(this);
        previousQuestion_ly.setOnClickListener(this);
        addToFavoredQuestion_imgbtn.setOnClickListener(this);
        minus_imgbtn.setOnClickListener(this);
        cross_imgbtn.setOnClickListener(this);
        full_imgbtn.setOnClickListener(this);
        firstChoice_btn.setOnClickListener(this);
        secondChoice_btn.setOnClickListener(this);
        thirdChoice_btn.setOnClickListener(this);
        fourthChoice_btn.setOnClickListener(this);
        confirm_fab.setOnClickListener(this);
        confirm_fab.setOnClickListener(this);
    }

    private ArrayList<TestDefinitionObj> pageMaker() {
        ArrayList<TestDefinitionObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.test_definition)).getJSONArray("testDefinition");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);
                ArrayList<TestObj> questionInfo = new ArrayList<>();

                int idTest = event.getInt("idTest");
                int questionNo = event.getInt("questionNo");
                JSONArray questionInfoArray = event.getJSONArray("questionInfo");
                int percentage = event.getInt("percentage");
                String level = event.getString("level");

                for (int j = 0; j < questionInfoArray.length(); j++) {
                    JSONArray array = questionInfoArray.getJSONArray(j);
                    questionInfo.add(new TestObj(array.getInt(0), array.getString(1)
                            , array.getString(2), array.getInt(3)));
                }

                result.add(new TestDefinitionObj(idTest, questionNo, questionInfo, percentage, level));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

    private void timer(long milliSeconds) {
        countDownTimer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                if (isPaused || isCanceled) {
                    /*CountDownTimer we will cancel the current instance*/
                    cancel();
                } else {
                    progressBar.setProgress(progressBar.getProgress() + ((float) 1 / time));
                    timer_tv.setText(String.valueOf(String.format(decimal,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                    timeRemaining = millisUntilFinished;
                }
            }

            public void onFinish() {
                timer_tv.setText("0 : 0");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1500);
                if (answerList.size() > 0) {
                    addAnswer();
                } else {
                    for (int i = 0; i < pageTest.getQuestionInfo().size(); i++) {
                        answerList.add(0);
                    }
                }
                compareAnswers();
                showDialogResults();
            }
        }.start();
    }

    private void updatePage() {
        numberOfQuestions_tv.setText(String.valueOf((question + 1) + " / " + (totalQuestion + 1)));
        showQuestions(question);
        questionZoomable.update();
        hideShowBackForward(question + 1);
        Log.i("Answer", answerList.toString());
        if (answerList.size() >= (question + 1)) {
            switch (answerList.get(question)) {
                case 1:
                    whichAnswer = 1;
                    selectButton(whichAnswer);
                    break;
                case 2:
                    whichAnswer = 2;
                    selectButton(whichAnswer);
                    break;
                case 3:
                    whichAnswer = 3;
                    selectButton(whichAnswer);
                    break;
                case 4:
                    whichAnswer = 4;
                    selectButton(whichAnswer);
                    break;
                default:
                    whichAnswer = 0;
                    selectButton(whichAnswer);
                    break;
            }
        } else {
            whichAnswer = 0;
            selectButton(whichAnswer);
        }
    }

    private void showQuestions(int position) {
        if (db.isQuestionFavored(pageTest.getQuestionInfo().get(position).getIdQuestion())) {
            addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark);
        } else {
            addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark_plus_outline);
        }
        showQuestionState(db.selectQuestionState(pageTest.getQuestionInfo().get(position).getIdQuestion()));
        new ImageLoad("TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionInfo().get(position).getQuestionImage(), question_imgv);
    }

    private void addAnswer() {
        if (answerList.size() > 0) {
            if (answerList.size() < (question + 1)) {
                answerList.add(whichAnswer);
                isAnswered = true;
            } else {
                isAnswered = true;
                answerList.set(question, whichAnswer);
            }
        } else {
            if (whichAnswer != 0) {
                isAnswered = false;
                answerList.add(whichAnswer);
            } else {
                isAnswered = false;
                answerList.add(0);
            }
        }
    }

    private void compareAnswers() {
        for (int i = 0; i < pageTest.getQuestionInfo().size(); i++) {
            if (pageTest.getQuestionInfo().get(i).getKey() == answerList.get(i)) {
                correctAnsweredList.add(pageTest.getQuestionInfo().get(i).getKey());
            } else if (answerList.get(i).equals(0)) {
                unAnsweredList.add(0);
            } else {
                inCorrectAnsweredList.add(i);
            }
        }
    }

    private void addQuestionState() {
        if (isAnswered && !isMinus && !isCross) {
            questionState = 0;
        } else if (isAnswered && !isCross) {
            questionState = 1;
        } else if (isAnswered && !isMinus) {
            questionState = 2;
        } else if (!isAnswered && !isMinus && !isCross) {
            questionState = 3;
        } else if (!isAnswered && !isCross) {
            questionState = 4;
        } else if (!isAnswered && !isMinus) {
            questionState = 5;
        }
        if (db.isQuestionStateCreated(pageTest.getQuestionInfo().get(question).getIdQuestion())) {
            db.questionStateUpdate(pageTest.getQuestionInfo().get(question).getIdQuestion(), questionState);
        } else {
            db.questionStateInsert(pageTest.getQuestionInfo().get(question).getIdQuestion(), questionState);
        }
    }

    private void showQuestionState(int state) {
        switch (state) {
            case 0:
                isMinus = false;
                isCross = false;
                isAnswered = true;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                break;
            case 1:
                isMinus = true;
                isCross = false;
                isAnswered = true;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.green));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                break;
            case 2:
                isMinus = false;
                isCross = true;
                isAnswered = true;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.accentColor));
                break;
            case 3:
                isMinus = false;
                isCross = false;
                isAnswered = false;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                break;
            case 4:
                isMinus = true;
                isCross = false;
                isAnswered = false;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.green));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                break;
            case 5:
                isMinus = false;
                isCross = true;
                isAnswered = false;
                minus_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.lightGray));
                cross_imgbtn.setColorFilter(ContextCompat.getColor(context, R.color.accentColor));
                break;
        }
    }

    private void hideShowBackForward(int question) {
        if (question == 1) {
            previousQuestion_ly.setVisibility(View.INVISIBLE);
        } else if (question > 1 && question < 10) {
            nextQuestion_ly.setVisibility(View.VISIBLE);
            previousQuestion_ly.setVisibility(View.VISIBLE);
        } else if (question == 10) {
            nextQuestion_ly.setVisibility(View.INVISIBLE);
        }
    }

    private void showDialogResults() {
        TextView title_tv = (TextView) dialogResults.findViewById(R.id.title_tv);
        TextView sub_title_tv = (TextView) dialogResults.findViewById(R.id.sub_title_tv);
        TextView results_tv = (TextView) dialogResults.findViewById(R.id.results_tv);
        Button seeResults_btn = (Button) dialogResults.findViewById(R.id.seeResults_btn);
        Button cancel_btn = (Button) dialogResults.findViewById(R.id.cancel_btn);

        title_tv.setText(String.valueOf("نتایج "));
        sub_title_tv.setText(String.valueOf("شما به " + (correctAnsweredList.size() * 10) + "٪ از سوالات پاسخ داده اید "));
        results_tv.setText(String.valueOf(correctAnsweredList.size() + " پاسخ صحیح " + "\n" + unAnsweredList.size()
                + " سوال جواب نداده" + "\n" + inCorrectAnsweredList.size() + " پاسخ اشتباه"));

        seeResults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResults.dismiss();
                finish();
                startActivity(new Intent(context, CourseAnswersActivity.class).putExtra("idTest", idTest));
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResults.dismiss();
                finish();
            }
        });

        db.historyInsert(new HistoryObj(idTest, testName, String.valueOf(time - TimeUnit.MILLISECONDS.toSeconds(timeRemaining)),
                String.valueOf(correctAnsweredList.size() * 10), correctAnsweredList.size(),
                inCorrectAnsweredList.size(), unAnsweredList.size(), String.valueOf(System.currentTimeMillis() / 1000)));

        pausePlay_imgbtn.setVisibility(View.GONE);
        isCanceled = true;

        dialogResults.show();
    }

    private void showDialogSaveTest() {
        Button saveTest_btn = (Button) dialogSaveTest.findViewById(R.id.saveTest_btn);
        Button cancel_btn = (Button) dialogSaveTest.findViewById(R.id.cancel_btn);

        saveTest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSaveTest.dismiss();
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSaveTest.dismiss();
            }
        });

        dialogSaveTest.show();
    }

    private void selectButton(int answer) {
        switch (answer) {
            case 1:
                firstChoice_btn.setSelected(true);
                firstChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.white));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                break;
            case 2:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                secondChoice_btn.setSelected(true);
                secondChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.white));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                break;
            case 3:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                thirdChoice_btn.setSelected(true);
                thirdChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.white));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                break;
            case 4:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                fourthChoice_btn.setSelected(true);
                fourthChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            default:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(ContextCompat.getColor(context, R.color.darkPrimaryColor));
                break;
        }
    }

    private void blurPage() {
        if (isPaused) {
            pauseLayout.setVisibility(View.VISIBLE);
            Bitmap bm = BlurBuilder.blur(BlurBuilder.loadBitmapFromView(rootView));
            pauseLayout.setImageBitmap(bm);
            nextQuestion_ly.setClickable(false);
            previousQuestion_ly.setClickable(false);
            addToFavoredQuestion_imgbtn.setClickable(false);
            minus_imgbtn.setClickable(false);
            cross_imgbtn.setClickable(false);
            firstChoice_btn.setClickable(false);
            secondChoice_btn.setClickable(false);
            thirdChoice_btn.setClickable(false);
            fourthChoice_btn.setClickable(false);
            full_imgbtn.setClickable(false);
        } else {
            pauseLayout.setVisibility(View.GONE);
            nextQuestion_ly.setClickable(true);
            previousQuestion_ly.setClickable(true);
            addToFavoredQuestion_imgbtn.setClickable(true);
            minus_imgbtn.setClickable(true);
            cross_imgbtn.setClickable(true);
            firstChoice_btn.setClickable(true);
            secondChoice_btn.setClickable(true);
            thirdChoice_btn.setClickable(true);
            fourthChoice_btn.setClickable(true);
        }
    }

    private void initMenu(AppCompatImageButton btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, btn);
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.course_question)
                .triggerOnAnchorClick(false)
                .setOnClick(this)
                .build();
        droppyMenu.show();
    }
}
