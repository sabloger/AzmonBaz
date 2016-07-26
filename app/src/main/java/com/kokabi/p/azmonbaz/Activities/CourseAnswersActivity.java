package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.ImageLoad;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestDefinitionObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    TestDefinitionObj pageTest;
    int idTest = 0, answer = 0, totalQuestion = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_answers);

        context = this;
        AppController.setActivityContext(CourseAnswersActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idTest = bundle.getInt("idTest", 0);
        }

        for (int i = 0; i < pageMaker().size(); i++) {
            if (pageMaker().get(i).getIdTest() == idTest) {
                pageTest = new TestDefinitionObj(pageMaker().get(i).getIdTest(), pageMaker().get(i).getQuestionNo()
                        , pageMaker().get(i).getQuestionInfo(), pageMaker().get(i).getPercentage());
            }
        }

        totalQuestion = pageTest.getQuestionNo() - 1;
        hideShowBackForward(answer + 1);

        showAnswers(0);
        answerZoomable = new PhotoViewAttacher(answer_imgv);
        title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_imgbtn:
                finish();
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

    private void updatePage() {
        title_tv.setText(String.valueOf("پاسخ سوال " + (answer + 1)));
        showAnswers(answer);
        answerZoomable.update();
        hideShowBackForward(answer + 1);
    }

    private void showAnswers(int position) {
        new ImageLoad("TestDefinitions/" + pageTest.getIdTest() + "/a/" + pageTest.getQuestionInfo().get(position).getAnswerImage(), answer_imgv);
/*        File root = android.os.Environment.getExternalStorageDirectory();
        File imgFile = new File(root.getAbsolutePath() + Constants.appFolder + Constants.testDefinitionsFolder
                + Constants.testFolder + Constants.questionsFolder + "/" + pageTest.getQuestionImages().get(position));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Drawable drawable = new BitmapDrawable(context.getResources(), myBitmap);
            question_imgv.setImageDrawable(drawable);
        }*/
    }

    private void hideShowBackForward(int question) {
        if (question == 1) {
            previousQuestion_ly.setVisibility(View.GONE);
        } else if (question > 1 && question < 10) {
            nextQuestion_ly.setVisibility(View.VISIBLE);
            previousQuestion_ly.setVisibility(View.VISIBLE);
        } else if (question == 10) {
            nextQuestion_ly.setVisibility(View.GONE);
        }
    }

    private ArrayList<TestDefinitionObj> pageMaker() {
        ArrayList<TestDefinitionObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.test_definition)).getJSONArray("testDefinition");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);
                result.add(new Gson().fromJson(event.toString(), TestDefinitionObj.class));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

}
