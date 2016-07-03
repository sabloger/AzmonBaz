package com.kokabi.p.azmonbaz.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.R;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    TextView timer_tv, numberOfQuestions_tv;
    AppCompatImageButton close_imgbtn;
    ProgressView progressBar;
    LinearLayout nextQuestion_ly, previousQuestion_ly;
    ImageView question_imgv;
    Button firstChoice_btn, secondChoice_btn, thirdChoice_btn, fourthChoice_btn;
    FloatingActionButton confirm_fab, numberOfQuestions_fab;

    /*Activity Values*/
    PhotoViewAttacher questionZoomable;
    CountDownTimer countDownTimer;
    boolean hasNegativePoint = false;
    int idTest = 0, time = 0, question = 0, totalQuestion = 0, whichAnswer = 0;
    TestDefinitionObj pageTest;
    ArrayList<Integer> answerList = new ArrayList<>();
    ArrayList<Integer> correctAnsweredList = new ArrayList<>();
    ArrayList<Integer> unAnsweredList = new ArrayList<>();
    ArrayList<Integer> inCorrectAnsweredList = new ArrayList<>();

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idTest = bundle.getInt("idTest", 0);
            time = bundle.getInt("time", 0);
            hasNegativePoint = bundle.getBoolean("hasNegativePoint", false);
        }

        for (int i = 0; i < pageMaker().size(); i++) {
            if (pageMaker().get(i).getIdTest() == idTest) {
                pageTest = new TestDefinitionObj(pageMaker().get(i).getIdTest(), pageMaker().get(i).getQuestionNo()
                        , pageMaker().get(i).getQuestionImages(), pageMaker().get(i).getAnswerImages(), pageMaker().get(i).getKeys()
                        , pageMaker().get(i).getPercentage(), pageMaker().get(i).getLevel());
            }
        }

        timer(time * 1000);

        progressBar.setProgress(0f);
        progressBar.start();

        totalQuestion = pageTest.getQuestionNo() - 1;


        showQuestions(0);
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

    private ArrayList<TestDefinitionObj> pageMaker() {
        ArrayList<TestDefinitionObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.test_definition)).getJSONArray("testDefinition");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);
                ArrayList<String> questionImages = new ArrayList<>();
                ArrayList<String> answerImages = new ArrayList<>();
                ArrayList<Integer> keys = new ArrayList<>();

                int idTest = event.getInt("idTest");
                int questionNo = event.getInt("questionNo");
                JSONArray questionImageArray = event.getJSONArray("questionImages");
                JSONArray answerImageArray = event.getJSONArray("answerImages");
                JSONArray keysArray = event.getJSONArray("keys");
                int percentage = event.getInt("percentage");
                String level = event.getString("level");

                for (int j = 0; j < questionImageArray.length(); j++) {
                    questionImages.add(questionImageArray.get(j).toString());
                }

                for (int k = 0; k < answerImageArray.length(); k++) {
                    answerImages.add(answerImageArray.get(k).toString());
                }

                for (int m = 0; m < keysArray.length(); m++) {
                    keys.add(Integer.parseInt(keysArray.get(m).toString()));
                }

                result.add(new TestDefinitionObj(idTest, questionNo, questionImages, answerImages, keys, percentage, level));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

    private void timer(long milliSeconds) {
        countDownTimer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressBar.getProgress() + ((float) 1 / time));
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
        showQuestions(question);
        questionZoomable.update();
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
        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionImages().get(position));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            question_imgv.setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.e(CourseQuestionsActivity.class.getName(), e.getMessage());
        }
/*        File root = android.os.Environment.getExternalStorageDirectory();
        File imgFile = new File(root.getAbsolutePath() + Constants.appFolder + Constants.testDefinitionsFolder
                + Constants.testFolder + Constants.questionsFolder + "/" + pageTest.getQuestionImages().get(position));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
            question_imgv.setImageDrawable(drawable);
        }*/
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

    private void compareAnswers() {
        for (int i = 0; i < pageTest.getKeys().size(); i++) {
            if (pageTest.getKeys().get(i).equals(answerList.get(i))) {
                correctAnsweredList.add(pageTest.getKeys().get(i));
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

        title_tv.setText(String.valueOf("نتایج "));
        sub_title_tv.setText(String.valueOf("شما به " + (correctAnsweredList.size() * 10) + "٪ از سوالات پاسخ داده اید "));
        results_tv.setText(String.valueOf(correctAnsweredList.size() + " پاسخ صحیح " + "\n" + unAnsweredList.size()
                + " سوال جواب نداده" + "\n" + inCorrectAnsweredList.size() + " پاسخ اشتباه"));

        seeResults_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResults.dismiss();
                finish();
//                startActivity(new Intent(context, CourseAnswersActivity.class));
            }
        });

        dialogResults.show();
    }

    private void selectButton(int answer) {
        switch (answer) {
            case 1:
                firstChoice_btn.setSelected(true);
                firstChoice_btn.setTextColor(Color.parseColor("#FFFFFF"));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case 2:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                secondChoice_btn.setSelected(true);
                secondChoice_btn.setTextColor(Color.parseColor("#FFFFFF"));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case 3:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                thirdChoice_btn.setSelected(true);
                thirdChoice_btn.setTextColor(Color.parseColor("#FFFFFF"));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case 4:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                fourthChoice_btn.setSelected(true);
                fourthChoice_btn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            default:
                firstChoice_btn.setSelected(false);
                firstChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                secondChoice_btn.setSelected(false);
                secondChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                thirdChoice_btn.setSelected(false);
                thirdChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                fourthChoice_btn.setSelected(false);
                fourthChoice_btn.setTextColor(Color.parseColor("#D32F2F"));
                break;
        }
    }

}
