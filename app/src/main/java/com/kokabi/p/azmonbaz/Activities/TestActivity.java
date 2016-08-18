package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Adapters.TestRVAdapter;
import com.kokabi.p.azmonbaz.Components.DialogGeneral;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

/**
 * Created by P.Kokabi on 6/30/16.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    CoordinatorLayout mainContent;
    TextView title_tv;
    AppCompatImageButton back_imgbtn;
    RecyclerView testRV;

    /*Activity Values*/
    TestRVAdapter testRVAdapter;
    int idCategory;
    String breadCrumb;
    ArrayList<TestsTitleObj> pageTests = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        context = this;
        AppController.setActivityContext(TestActivity.this, this);
        EventBus.getDefault().register(this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        // use a linear layout manager
        testRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        testRV.setHasFixedSize(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idCategory = bundle.getInt("idCat", 0);
            breadCrumb = bundle.getString("breadCrumb", "");
        }

        for (int i = 0; i < Constants.totalTestTitles.size(); i++) {
            if (Constants.totalTestTitles.get(i).getIdCat() == idCategory) {
                pageTests.add(Constants.totalTestTitles.get(i));
            }
        }
        // TODO Remove when Project is finished
        if (pageTests.size() == 0) {
            finish();
        }

        Collections.sort(pageTests);
        testRVAdapter = new TestRVAdapter(pageTests, false, breadCrumb);
        testRV.setAdapter(testRVAdapter);

        // TODO Remove when Project is finished
        if (pageTests.size() != 0) {
            for (int i = 0; i < Constants.totalCategories.size(); i++) {
                if (Constants.totalCategories.get(i).getIdCat() == pageTests.get(0).getIdCat()) {
                    title_tv.setText(Constants.totalCategories.get(i).getCatName());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        testRVAdapter.updateTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.freeMemory();
        EventBus.getDefault().unregister(this);
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

        testRV = (RecyclerView) findViewById(R.id.childRV);

        setOnClick();
    }

    private void setOnClick() {
        back_imgbtn.setOnClickListener(this);
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "isResume":
                new DialogGeneral(context.getResources().getString(R.string.resumeTestTitle)
                        , context.getResources().getString(R.string.startTest)
                        , context.getResources().getString(R.string.cancel)) {
                    @Override
                    public void onConfirm() {
                        context.startActivity(new Intent(context, CourseQuestionsActivity.class)
                                .putExtra("idTest", event.getTestsTitleObj().getIdTest())
                                .putExtra("time", event.getTestsTitleObj().getTime())
                                .putExtra("testName", event.getTestsTitleObj().getTestName())
                                .putExtra("hasNegativePoint", event.getTestsTitleObj().isHasNegativePoint())
                                .putExtra("isResumeTest", false)
                                .putExtra("initTime", event.getTestsTitleObj().getTime())
                                .putExtra("breadCrumb", event.getBreadCrumb()));
                    }
                }.show();
                break;
        }
    }
}
