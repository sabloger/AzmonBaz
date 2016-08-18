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

import com.kokabi.p.azmonbaz.Adapters.TreeRVAdapter;
import com.kokabi.p.azmonbaz.EventBuss.GeneralMSB;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

/**
 * Created by P.Kokabi on 6/30/16.
 */
public class TreeActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    CoordinatorLayout mainContent;
    TextView title_tv;
    AppCompatImageButton back_imgbtn;
    RecyclerView childRV;

    /*Activity Values*/
    TreeRVAdapter treeRVAdapter;
    int idCategory;
    String breadCrumb;
    private boolean isIntent = false;
    ArrayList<CategoryObj> pageCategories = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        context = this;
        AppController.setActivityContext(TreeActivity.this, this);
        EventBus.getDefault().register(this);
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idCategory = bundle.getInt("idCat", 0);
            breadCrumb = bundle.getString("breadCrumb", "");
        }

        // use a linear layout manager
        childRV.setLayoutManager(new LinearLayoutManager(context));
        // in content do not change the layout size of the RecyclerView
        childRV.setHasFixedSize(true);

        for (int i = 0; i < Constants.totalCategories.size(); i++) {
            if (Constants.totalCategories.get(i).getIdParent() == idCategory) {
                pageCategories.add(Constants.totalCategories.get(i));
            }
        }

        Collections.sort(pageCategories);
        treeRVAdapter = new TreeRVAdapter(context, pageCategories, breadCrumb);
        childRV.setAdapter(treeRVAdapter);

        for (int i = 0; i < Constants.totalCategories.size(); i++) {
            if (Constants.totalCategories.get(i).getIdCat() == pageCategories.get(0).getIdParent()) {
                title_tv.setText(Constants.totalCategories.get(i).getCatName());
            }
        }

        childRV.addOnItemTouchListener(new TreeRVAdapter(context, new TreeRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isIntent = false;
                for (int i = 0; i < Constants.totalCategories.size(); i++) {
                    if (pageCategories.get(position).getIdCat() == Constants.totalCategories.get(i).getIdParent()) {
                        isIntent = true;
                    }
                }
                if (isIntent) {
                    startActivity(new Intent(context, TreeActivity.class)
                            .putExtra("idCat", pageCategories.get(position).getIdCat())
                            .putExtra("breadCrumb", breadCrumb + " - " + pageCategories.get(position).getCatName()));
                } else {
                    startActivity(new Intent(context, TestActivity.class)
                            .putExtra("idCat", pageCategories.get(position).getIdCat())
                            .putExtra("breadCrumb", breadCrumb + " - " + pageCategories.get(position).getCatName()));
                }
            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Constants.freeMemory();
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

        childRV = (RecyclerView) findViewById(R.id.childRV);

        setOnClick();
    }

    private void setOnClick() {
        back_imgbtn.setOnClickListener(this);
    }

    public void onEvent(final GeneralMSB event) {
        switch (event.getMessage()) {
            case "testAnswered":
                treeRVAdapter.notifyDataSetChanged();
                break;
        }
    }

}
