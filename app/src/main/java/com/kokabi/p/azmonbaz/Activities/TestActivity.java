package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Adapters.TestRVAdapter;
import com.kokabi.p.azmonbaz.Fragments.CoursesFragment;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Help.ReadJSON;
import com.kokabi.p.azmonbaz.Objects.TestsObj;
import com.kokabi.p.azmonbaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by P.Kokabi on 6/30/16.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    CoordinatorLayout mainContent;
    TextView title_tv;
    AppCompatImageButton back_imgbtn;
    ListView testLV;
    RecyclerView testRV;

    /*Activity Values*/
    TestRVAdapter testRVAdapter;
    int idCategory;
    ArrayList<TestsObj> pageTests = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        context = this;
        AppController.setActivityContext(TestActivity.this, this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        // use a linear layout manager
        testRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        testRV.setHasFixedSize(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idCategory = bundle.getInt("idCat", 0);
        }

        for (int i = 0; i < pageMaker().size(); i++) {
            if (pageMaker().get(i).getIdCat() == idCategory) {
                pageTests.add(pageMaker().get(i));
            }
        }
        // TODO Remove when Project is finished
        if (pageTests.size() == 0) {
            finish();
        }

        Collections.sort(pageTests);
        testRVAdapter = new TestRVAdapter(pageTests);
        testRV.setAdapter(testRVAdapter);

        // TODO Remove when Project is finished
        if (pageTests.size() != 0) {
            for (int i = 0; i < Constants.totalCategories.size(); i++) {
                if (Constants.totalCategories.get(i).getIdCat() == pageTests.get(0).getIdCat()) {
                    title_tv.setText(Constants.totalCategories.get(i).getCatName());
                }
            }
        }

        testRV.addOnItemTouchListener(new TestRVAdapter(context, new TestRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
/*                startActivity(new Intent(context, CourseQuestionsActivity.class)
                        .putExtra("idTest", pageTests.get(position).getIdTest())
                        .putExtra("time", pageTests.get(position).getTime())
                        .putExtra("hasNegativePoint", pageTests.get(position).isHasNegativePoint()));*/
            }
        }));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imgbtn:
                finish();
                break;
        }
    }

    private void findViews() {
        title_tv = (TextView) findViewById(R.id.title_tv);

        back_imgbtn = (AppCompatImageButton) findViewById(R.id.back_imgbtn);

        testLV = (ListView) findViewById(R.id.childLV);

        testRV = (RecyclerView) findViewById(R.id.testRV);

        testLV.setVisibility(View.GONE);
        testRV.setVisibility(View.VISIBLE);

        setOnClick();
    }

    private void setOnClick() {
        back_imgbtn.setOnClickListener(this);
    }

    private ArrayList<TestsObj> pageMaker() {
        ArrayList<TestsObj> result = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONObject(ReadJSON.readRawResource(R.raw.tests)).getJSONArray("tests");

            int length = categoryArray.length();
            for (int i = 0; i < length; ++i) {
                JSONObject event = categoryArray.getJSONObject(i);

                int idCat = event.getInt("idCat");
                int idTest = event.getInt("idTest");
                String testName = event.getString("testName");
                String description = event.getString("description");
                int time = event.getInt("time");
                boolean hasNegativePoint = event.getBoolean("hasNegativePoint");
                String testOrder = event.getString("testOrder");
                int questionCount = event.getInt("questionCount");

                result.add(new TestsObj(idCat, idTest, testName, description, time, hasNegativePoint, testOrder, questionCount));
            }

        } catch (JSONException e) {
            Log.e(CoursesFragment.class.getName(), e.getMessage());
        }
        return result;
    }

}
