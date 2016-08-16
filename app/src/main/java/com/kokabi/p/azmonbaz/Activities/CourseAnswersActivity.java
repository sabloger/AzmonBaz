package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kokabi.p.azmonbaz.DB.DataBase;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.ImageLoad;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by P.Kokabi on 6/23/16.
 */
public class CourseAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    DataBase db;

    CoordinatorLayout mainContent;
    TextView title_tv, answerState_tv, questionLevel_tv;
    AppCompatImageButton close_imgbtn, questionAnswer_imgbtn, addToFavoredQuestion_imgbtn, full_imgbtn;
    LinearLayout nextQuestion_ly, previousQuestion_ly;
    AppCompatImageView answer_imgv, answerState_imgv, questionLevel_imgv;

    /*Activity Values*/
    PhotoViewAttacher answerZoomable;
    TestDefinitionObj pageTest;
    HashMap<Integer, Integer> answerList = new HashMap<>();
    boolean isAnswer = true;
    int idTest = 0, answer = 0, totalQuestion = 9;
    String testName = "", breadCrumb = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_answers);

        context = this;
        AppController.setActivityContext(CourseAnswersActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);
        db = new DataBase();

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idTest = bundle.getInt("idTest", 0);
            testName = bundle.getString("testName", "");
            breadCrumb = bundle.getString("breadCrumb", "");
        }

        for (int i = 0; i < pageMaker().size(); i++) {
            if (pageMaker().get(i).getIdTest() == idTest) {
                pageTest = new TestDefinitionObj(pageMaker().get(i).getIdTest(), pageMaker().get(i).getQuestionNo()
                        , pageMaker().get(i).getQuestionInfo(), pageMaker().get(i).getPercentage());
                break;
            }
        }

        try {
            JSONObject json = new JSONObject(db.selectTestHistory(idTest));
            JSONArray names = json.names();
            for (int i = 0; i < names.length(); i++) {
                int key = names.getInt(i);
                answerList.put(key, json.getInt(String.valueOf(key)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        totalQuestion = pageTest.getQuestionNo() - 1;
        hideShowBackForward(answer + 1);

        answerZoomable = new PhotoViewAttacher(answer_imgv);
        updatePage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.freeMemory();
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
                    showAnswers(answer);
                    answerZoomable.update();
                } else {
                    isAnswer = false;
                    questionAnswer_imgbtn.setImageResource(R.drawable.ic_answer);
                    showAnswers(answer);
                    answerZoomable.update();
                }
                break;
            case R.id.full_imgbtn:
                if (!isAnswer) {
                    startActivity(new Intent(context, FullPageImageActivity.class)
                            .putExtra("srcImage", "TestDefinitions/" + pageTest.getIdTest() + "/q/"
                                    + pageTest.getQuestionInfo().get(answer).getQuestionImage())
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else {
                    startActivity(new Intent(context, FullPageImageActivity.class)
                            .putExtra("srcImage", "TestDefinitions/" + pageTest.getIdTest() + "/a/"
                                    + pageTest.getQuestionInfo().get(answer).getAnswerImage())
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
                break;
            case R.id.nextQuestion_ly:
                if (answer < totalQuestion) {
                    answer++;
                    updatePage();
                    answerZoomable.update();
                    title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
                }
                break;
            case R.id.previousQuestion_ly:
                if (answer > 0) {
                    answer--;
                    updatePage();
                    answerZoomable.update();
                    title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
                }
                break;
            case R.id.addToFavoredQuestion_imgbtn:
                if (db.isQuestionFavored(pageTest.getQuestionInfo().get(answer).getIdQuestion(), testName, breadCrumb)) {
                    addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark_outline);
                    db.favoredQuestionDelete(pageTest.getQuestionInfo().get(answer).getIdQuestion(), breadCrumb, testName);
                } else {
                    db.favoredQuestionInsert(new TestObj(testName, pageTest.getQuestionInfo().get(answer).getIdQuestion(),
                            String.valueOf("TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionInfo().get(answer).getQuestionImage()),
                            String.valueOf("TestDefinitions/" + pageTest.getIdTest() + "/a/" + pageTest.getQuestionInfo().get(answer).getAnswerImage()),
                            pageTest.getQuestionInfo().get(answer).getKey(), breadCrumb));
                    addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark);
                }
                break;
        }
    }

    private void findViews() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        answerState_tv = (TextView) findViewById(R.id.answerState_tv);
        questionLevel_tv = (TextView) findViewById(R.id.questionLevel_tv);

        close_imgbtn = (AppCompatImageButton) findViewById(R.id.close_imgbtn);
        questionAnswer_imgbtn = (AppCompatImageButton) findViewById(R.id.questionAnswer_imgbtn);
        addToFavoredQuestion_imgbtn = (AppCompatImageButton) findViewById(R.id.addToFavoredQuestion_imgbtn);
        full_imgbtn = (AppCompatImageButton) findViewById(R.id.full_imgbtn);

        nextQuestion_ly = (LinearLayout) findViewById(R.id.nextQuestion_ly);
        previousQuestion_ly = (LinearLayout) findViewById(R.id.previousQuestion_ly);

        answer_imgv = (AppCompatImageView) findViewById(R.id.answer_imgv);
        answerState_imgv = (AppCompatImageView) findViewById(R.id.answerState_imgv);
        questionLevel_imgv = (AppCompatImageView) findViewById(R.id.questionLevel_imgv);

        setOnClick();
    }

    private void setOnClick() {
        close_imgbtn.setOnClickListener(this);
        nextQuestion_ly.setOnClickListener(this);
        previousQuestion_ly.setOnClickListener(this);
        questionAnswer_imgbtn.setOnClickListener(this);
        addToFavoredQuestion_imgbtn.setOnClickListener(this);
        full_imgbtn.setOnClickListener(this);
    }

    private void updatePage() {
        title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
        showAnswers(answer);
        answerZoomable.update();
        questionLevel(answer);
        questionState(answer);
        showQuestions(answer);
        hideShowBackForward(answer + 1);
        Constants.freeMemory();
    }

    private void showAnswers(int position) {
        if (isAnswer) {
            new ImageLoad("TestDefinitions/" + pageTest.getIdTest() + "/a/" + pageTest.getQuestionInfo().get(position).getAnswerImage(), answer_imgv);
        } else {
            new ImageLoad("TestDefinitions/" + pageTest.getIdTest() + "/q/" + pageTest.getQuestionInfo().get(position).getQuestionImage(), answer_imgv);
        }
    }

    private void questionLevel(int position) {
        switch (pageTest.getQuestionInfo().get(position).getLevel()) {
            case 0:
                questionLevel_tv.setText("سطح این سوال سخت می باشد");
                questionLevel_tv.setTextColor(ContextCompat.getColor(context, R.color.hard));
                questionLevel_imgv.setImageResource(R.drawable.ic_hard);
                break;
            case 1:
                questionLevel_tv.setText("سطح این سوال متوسط می باشد");
                questionLevel_tv.setTextColor(ContextCompat.getColor(context, R.color.medium));
                questionLevel_imgv.setImageResource(R.drawable.ic_medium);
                break;
            case 2:
                questionLevel_tv.setText("سطح این سوال آسان می باشد");
                questionLevel_tv.setTextColor(ContextCompat.getColor(context, R.color.easy));
                questionLevel_imgv.setImageResource(R.drawable.ic_easy);
                break;
        }
    }

    private void questionState(int position) {
        if (pageTest.getQuestionInfo().get(position).getKey() == answerList.get(position + 1)) {
            answerState_tv.setText(String.valueOf("به این سوال پاسخ صحیح داده اید (گزینه" + answerList.get(position + 1) + ")"));
            answerState_tv.setTextColor(ContextCompat.getColor(context, R.color.easy));
            answerState_imgv.setImageResource(R.drawable.ic_correct_answer);
            answerState_imgv.setColorFilter(ContextCompat.getColor(context, R.color.easy));
        } else if (answerList.get(position + 1) == 0) {
            answerState_tv.setText("به این سوال پاسخ نداده اید");
            answerState_tv.setTextColor(ContextCompat.getColor(context, R.color.darkGray));
            answerState_imgv.setImageResource(R.drawable.ic_unanswered);
            answerState_imgv.setColorFilter(ContextCompat.getColor(context, R.color.darkGray));
        } else {
            answerState_tv.setText(String.valueOf("به این سوال پاسخ اشتباه داده اید (گزینه" + answerList.get(position + 1) + ")"));
            answerState_tv.setTextColor(ContextCompat.getColor(context, R.color.hard));
            answerState_imgv.setImageResource(R.drawable.ic_incorrect_answer);
            answerState_imgv.setColorFilter(ContextCompat.getColor(context, R.color.hard));
        }
    }

    private void showQuestions(int position) {
        if (db.isQuestionFavored(pageTest.getQuestionInfo().get(position).getIdQuestion(), testName, breadCrumb)) {
            addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark);
        } else {
            addToFavoredQuestion_imgbtn.setImageResource(R.drawable.ic_bookmark_outline);
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

    private ArrayList<TestDefinitionObj> pageMaker() {
        ArrayList<TestDefinitionObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource("test_definition.json")).getJSONArray("testDefinition");

            for (int i = 0; i < categoryArray.length(); ++i) {
                JSONObject event = categoryArray.getJSONObject(i);
                result.add(new Gson().fromJson(event.toString(), TestDefinitionObj.class));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

}
