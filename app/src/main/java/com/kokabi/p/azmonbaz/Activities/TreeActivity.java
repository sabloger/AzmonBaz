package com.kokabi.p.azmonbaz.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kokabi.p.azmonbaz.Adapters.TreeLVAdapter;
import com.kokabi.p.azmonbaz.Help.AppController;
import com.kokabi.p.azmonbaz.Help.Constants;
import com.kokabi.p.azmonbaz.Objects.CategoryObj;
import com.kokabi.p.azmonbaz.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by P.Kokabi on 6/30/16.
 */
public class TreeActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    CoordinatorLayout mainContent;
    TextView title_tv;
    AppCompatImageButton back_imgbtn;
    ListView childLV;

    /*Activity Values*/
    TreeLVAdapter treeLVAdapter;
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
        mainContent = (CoordinatorLayout) findViewById(R.id.mainContent);

        findViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idCategory = bundle.getInt("idCat", 0);
            breadCrumb = bundle.getString("breadCrumb", "");
        }

        for (int i = 0; i < Constants.totalCategories.size(); i++) {
            if (Constants.totalCategories.get(i).getIdParent() == idCategory) {
                pageCategories.add(Constants.totalCategories.get(i));
            }
        }

        Collections.sort(pageCategories);
        treeLVAdapter = new TreeLVAdapter(context, pageCategories, breadCrumb);
        childLV.setAdapter(treeLVAdapter);

        for (int i = 0; i < Constants.totalCategories.size(); i++) {
            if (Constants.totalCategories.get(i).getIdCat() == pageCategories.get(0).getIdParent()) {
                title_tv.setText(Constants.totalCategories.get(i).getCatName());
            }
        }

        childLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        childLV = (ListView) findViewById(R.id.childLV);

        setOnClick();
    }

    private void setOnClick() {
        back_imgbtn.setOnClickListener(this);
    }

}
